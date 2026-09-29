# JBDL9_E-Wallet

This project is a Spring Boot microservice-based e-wallet application with:
- OnboardingService
- WalletService
- TransactionService
- NotificationService
- Shared CommonService

## Local prerequisite stack

The app depends on MySQL, Redis, and Kafka. You can start them locally with Docker Compose:

```bash
docker compose up -d
```

This will start:
- MySQL on `localhost:3306`
- Redis on `localhost:6379`
- Zookeeper on `localhost:2181`
- Kafka on `localhost:9092`

## Environment variables

The repo includes a `.env` file with default local values. Update it before deployment with your real credentials.

```bash
cp .env.example .env
# or use the default .env already created in the repo
```

## Build and run services

Build the shared library first:

```bash
cd CommonService
mvn clean install
cd ..
```

Then build the services:

```bash
cd OnboardingService && mvn clean package && java -jar target/OnboardingService-0.0.1-SNAPSHOT.jar
cd ../WalletService && mvn clean package && java -jar target/WalletService-0.0.1-SNAPSHOT.jar
cd ../TransactionService && mvn clean package && java -jar target/TransactionService-0.0.1-SNAPSHOT.jar
cd ../NotificationService && mvn clean package && java -jar target/NotificationService-0.0.1-SNAPSHOT.jar
```

## Health checks

```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
```

## Notes

- Do not commit the real `.env` file to source control.
- Use a Gmail app password for `MAIL_PASSWORD` instead of your normal Gmail password.
- For production, replace local defaults with secure secrets and deploy behind TLS.
