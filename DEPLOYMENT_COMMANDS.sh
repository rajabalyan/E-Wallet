#!/bin/bash
# E-Wallet Complete Deployment Script
# Run this script from the project root directory
# Usage: bash DEPLOYMENT_COMMANDS.sh

set -e  # Exit on any error

echo "=========================================="
echo "E-Wallet Deployment Script"
echo "=========================================="

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"

if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
fi

if ! command -v java &> /dev/null; then
    echo -e "${RED}Java is not installed. Please install Java 17+.${NC}"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Maven is not installed. Please install Maven 3.6+.${NC}"
    exit 1
fi

echo -e "${GREEN}✓ All prerequisites found${NC}\n"

# Step 1: Start infrastructure services
echo -e "${YELLOW}Step 1: Starting Docker services (MySQL, Redis, Kafka, Zookeeper)...${NC}"
docker-compose down -v 2>/dev/null || true  # Clean up any existing containers
docker-compose up -d

# Wait for services to be healthy
echo -e "${YELLOW}Waiting for services to be healthy (this may take 60 seconds)...${NC}"
sleep 10

max_attempts=30
attempt=0
all_healthy=false

while [ $attempt -lt $max_attempts ]; do
    attempt=$((attempt + 1))
    if docker-compose exec -T mysql mysqladmin ping -h localhost -uroot -proot_secure_password_2024 &> /dev/null && \
       docker-compose exec -T redis redis-cli ping &> /dev/null 2>&1 && \
       docker-compose exec -T zookeeper echo ruok | nc localhost 2181 &> /dev/null; then
        all_healthy=true
        break
    fi
    echo "  Attempt $attempt/$max_attempts - Waiting for services..."
    sleep 2
done

if [ "$all_healthy" = true ]; then
    echo -e "${GREEN}✓ All Docker services are healthy${NC}\n"
else
    echo -e "${RED}✗ Docker services failed to become healthy. Check docker-compose logs.${NC}"
    docker-compose logs
    exit 1
fi

# Step 2: Build CommonService
echo -e "${YELLOW}Step 2: Building CommonService (shared library)...${NC}"
cd CommonService
mvn clean install -DskipTests -q
cd ..
echo -e "${GREEN}✓ CommonService built successfully${NC}\n"

# Step 3: Build all services
echo -e "${YELLOW}Step 3: Building all microservices...${NC}"

services=("OnboardingService" "WalletService" "TransactionService" "NotificationService")

for service in "${services[@]}"; do
    echo "  Building $service..."
    cd "$service"
    mvn clean package -DskipTests -q
    cd ..
done

echo -e "${GREEN}✓ All services built successfully${NC}\n"

# Step 4: Display startup commands
echo -e "${GREEN}=========================================="
echo "✓ Build Complete!"
echo "==========================================${NC}\n"

echo -e "${YELLOW}The following services have been built and are ready to start:${NC}\n"

echo "Start each service in a separate terminal:\n"

echo -e "${GREEN}Terminal 1 - OnboardingService (Port 8081):${NC}"
echo "cd OnboardingService && java -jar target/OnboardingService-0.0.1-SNAPSHOT.jar\n"

echo -e "${GREEN}Terminal 2 - WalletService (Port 8083):${NC}"
echo "cd WalletService && java -jar target/WalletService-0.0.1-SNAPSHOT.jar\n"

echo -e "${GREEN}Terminal 3 - TransactionService (Port 8084):${NC}"
echo "cd TransactionService && java -jar target/TransactionService-0.0.1-SNAPSHOT.jar\n"

echo -e "${GREEN}Terminal 4 - NotificationService (Port 8082):${NC}"
echo "cd NotificationService && java -jar target/NotificationService-0.0.1-SNAPSHOT.jar\n"

echo -e "${YELLOW}After all services start, verify health in a new terminal:${NC}\n"
echo "curl http://localhost:8081/actuator/health"
echo "curl http://localhost:8082/actuator/health"
echo "curl http://localhost:8083/actuator/health"
echo "curl http://localhost:8084/actuator/health\n"

echo -e "${YELLOW}All should return: {\"status\":\"UP\"}\n${NC}"

echo -e "${GREEN}For more details, see QUICK_START.md${NC}\n"

echo -e "${YELLOW}Docker services are running. To stop them:${NC}"
echo "docker-compose down\n"
