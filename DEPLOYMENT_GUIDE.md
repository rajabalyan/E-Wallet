# E-Wallet Deployment Guide

This guide helps you deploy the E-Wallet microservices application.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL Server (5.7+ or 8.0+)
- Apache Kafka
- Redis
- Git

## Project Structure

```
E-Wallet/
├── CommonService/          # Shared utilities and JWT tokens
├── OnboardingService/      # User registration and login (Port 8081)
├── NotificationService/    # Email notifications (Port 8082)
├── WalletService/          # Wallet management (Port 8083)
├── TransactionService/     # Transaction processing (Port 8084)
└── frontend/               # Simple UI
```

## Step 1: Configure Environment Variables

Before running any service, set up environment variables. Copy `.env.example` to `.env` and update values:

### Linux/Mac:
```bash
export DB_USERNAME=root
export DB_PASSWORD=root
export DB_URL=jdbc:mysql://localhost:3306/jbdl9_wallet_userdb?createDatabaseIfNotExist=true
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=
export MAIL_USERNAME=rajabalyan9@gmail.com
export MAIL_PASSWORD=your-app-password-here
```

### Windows (PowerShell):
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="root"
$env:DB_URL="jdbc:mysql://localhost:3306/jbdl9_wallet_userdb?createDatabaseIfNotExist=true"
$env:KAFKA_BOOTSTRAP_SERVERS="localhost:9092"
$env:REDIS_HOST="localhost"
$env:REDIS_PORT="6379"
$env:REDIS_PASSWORD=""
$env:MAIL_USERNAME="rajabalyan9@gmail.com"
$env:MAIL_PASSWORD="your-app-password-here"
```

## Step 2: Install & Start Dependencies

### MySQL:
```bash
# Start MySQL server
# macOS with Homebrew:
brew services start mysql

# Linux:
sudo systemctl start mysql

# Windows: Start from Services or use MySQL installer
```

### Redis:
```bash
# macOS with Homebrew:
brew services start redis

# Linux:
sudo systemctl start redis-server

# Windows: Download and run redis-windows
# Or use WSL: wsl redis-server
```

### Kafka:
```bash
# Extract Kafka
tar -xzf kafka_2.13-3.5.0.tgz
cd kafka_2.13-3.5.0

# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# In another terminal, start Kafka
bin/kafka-server-start.sh config/server.properties
```

## Step 3: Build & Run Services

### 1. CommonService (Dependency - build first)
```bash
cd CommonService
mvn clean install
cd ..
```

### 2. OnboardingService
```bash
cd OnboardingService
mvn clean package
java -jar target/OnboardingService-0.0.1-SNAPSHOT.jar
# Runs on http://localhost:8081
```

### 3. WalletService
```bash
cd WalletService
mvn clean package
java -jar target/WalletService-0.0.1-SNAPSHOT.jar
# Runs on http://localhost:8083
```

### 4. TransactionService
```bash
cd TransactionService
mvn clean package
java -jar target/TransactionService-0.0.1-SNAPSHOT.jar
# Runs on http://localhost:8084
```

### 5. NotificationService
```bash
cd NotificationService
mvn clean package
java -jar target/NotificationService-0.0.1-SNAPSHOT.jar
# Runs on http://localhost:8082
```

## Step 4: Health Check

Check if all services are running:

```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
```

Each should return:
```json
{"status":"UP"}
```

## Step 5: Test the Application

### Create User (Onboarding Service)
```bash
curl --location 'http://localhost:8081/onboarding-service/create/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Test User",
    "email": "rajabalyan9@gmail.com",
    "password": "123456",
    "mobileNo": "9999999999",
    "dob": "01/01/1995",
    "userIdentifier": "AADHAAR_CARD",
    "userIdentifierValue": "1234567890123456"
}'
```

### User Login
```bash
curl --location 'http://localhost:8081/onboarding-service/user/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "9999999999",
    "password": "123456"
}'
```

### Get Wallet Balance
```bash
curl --location 'http://localhost:8083/wallet-service/get/balance' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE'
```

### Initiate Transaction
```bash
curl --location 'http://localhost:8084/txn-service/initiate/transaction' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE' \
--data '{
    "amount": 50.0,
    "purpose": "Payment",
    "receiver": "8890237687"
}'
```

## Step 6: Frontend UI

Open `frontend/index.html` in your browser (use Live Server for better experience)

## Troubleshooting

### "Connection refused" errors
- Ensure MySQL, Redis, and Kafka are running
- Check firewall settings
- Verify database credentials in environment variables

### "Table doesn't exist"
- Spring JPA should create tables automatically (ddl-auto=update)
- Check MySQL logs for errors
- Ensure database names match in configuration

### Email not sending
- Generate Gmail App Password: https://myaccount.google.com/apppasswords
- 2FA must be enabled on Google Account
- Don't use your regular Gmail password

### Kafka connection errors
- Ensure Kafka and Zookeeper are running
- Check `KAFKA_BOOTSTRAP_SERVERS` environment variable
- Review Kafka logs

## Production Deployment Checklist

- [ ] Change all default passwords
- [ ] Use strong database credentials
- [ ] Set up external MySQL, Redis, and Kafka servers
- [ ] Generate and configure Gmail App Password
- [ ] Configure SSL/TLS for all communications
- [ ] Set up proper logging and monitoring
- [ ] Use secrets management (AWS Secrets Manager, HashiCorp Vault, etc.)
- [ ] Configure backup strategies
- [ ] Set up CI/CD pipeline
- [ ] Load test before going live
- [ ] Set up alerting and health monitoring

## Support

For issues or questions, refer to individual service logs or check README files in each service directory.
