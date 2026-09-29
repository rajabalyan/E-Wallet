# Environment Variables Setup Guide

This guide explains how to set environment variables for the E-Wallet application.

## Quick Start

### Linux/macOS (Bash)
```bash
# Copy the template
cp .env.example .env

# Edit the .env file with your values
nano .env

# Load environment variables before running services
source .env

# Or set them individually:
export DB_USERNAME=root
export DB_PASSWORD=your_password
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export REDIS_HOST=localhost
export REDIS_PORT=6379
export MAIL_USERNAME=rajabalyan9@gmail.com
export MAIL_PASSWORD=your_app_password
```

### Windows (PowerShell)
```powershell
# Copy the template
Copy-Item .env.example .env

# Edit the .env file with your values
notpad .env

# Set environment variables:
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
$env:KAFKA_BOOTSTRAP_SERVERS="localhost:9092"
$env:REDIS_HOST="localhost"
$env:REDIS_PORT="6379"
$env:MAIL_USERNAME="rajabalyan9@gmail.com"
$env:MAIL_PASSWORD="your_app_password"
```

### Windows (CMD)
```cmd
set DB_USERNAME=root
set DB_PASSWORD=your_password
set KAFKA_BOOTSTRAP_SERVERS=localhost:9092
set REDIS_HOST=localhost
set REDIS_PORT=6379
set MAIL_USERNAME=rajabalyan9@gmail.com
set MAIL_PASSWORD=your_app_password
```

## Environment Variables Explained

### Database Configuration
- `DB_USERNAME` - MySQL username (default: root)
- `DB_PASSWORD` - MySQL password (default: root)
- `DB_URL` - MySQL connection URL with database name
- `JPA_DDL_AUTO` - Hibernate DDL strategy (default: update)
- `JPA_SHOW_SQL` - Show SQL queries in logs (default: false)

### Kafka Configuration
- `KAFKA_BOOTSTRAP_SERVERS` - Kafka server address:port (default: localhost:9092)
- `KAFKA_CONSUMER_GROUP` - Consumer group name (default: ewallet-consumer-group)

### Redis Configuration
- `REDIS_HOST` - Redis server address (default: localhost)
- `REDIS_PORT` - Redis server port (default: 6379)
- `REDIS_PASSWORD` - Redis password (default: empty)

### Mail Configuration (Gmail)
- `MAIL_HOST` - SMTP host (default: smtp.gmail.com)
- `MAIL_PORT` - SMTP port (default: 587)
- `MAIL_USERNAME` - Gmail address: rajabalyan9@gmail.com
- `MAIL_PASSWORD` - Gmail App Password (16 characters, NOT regular password)

### Server Configuration
- `SERVER_PORT` - Server port for each microservice
- `LOG_LEVEL` - Logging level (default: INFO)

### Wallet Configuration
- `WALLET_INITIAL_AMOUNT` - Initial wallet balance for new users (default: 100)

## How to Get Gmail App Password

1. Go to your Google Account: https://myaccount.google.com
2. Click "Security" in the left menu
3. Enable "2-Step Verification" if not already enabled
4. Click "App passwords" (near the bottom)
5. Select "Mail" and "Windows Computer" (or your device)
6. Google will generate a 16-character password
7. Copy this password and use it as `MAIL_PASSWORD`

**Important:** Use this generated App Password, NOT your regular Gmail password.

## Creating a .env File

```bash
# Example .env file
DB_USERNAME=root
DB_PASSWORD=SecurePassword123!
DB_URL=jdbc:mysql://localhost:3306/jbdl9_wallet_userdb?createDatabaseIfNotExist=true
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false

KAFKA_BOOTSTRAP_SERVERS=localhost:9092
KAFKA_CONSUMER_GROUP=ewallet-consumer-group

REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=rajabalyan9@gmail.com
MAIL_PASSWORD=abcd efgh ijkl mnop

SERVER_PORT=8081
LOG_LEVEL=INFO
WALLET_INITIAL_AMOUNT=100
```

## Important Notes

⚠️ **SECURITY WARNING:**
- Never commit `.env` file to version control
- Never share your environment variables
- Use `.env.example` as a template only
- Rotate passwords regularly in production
- Use a secrets management tool in production (AWS Secrets Manager, HashiCorp Vault, etc.)

## Verification

After setting environment variables, verify they are loaded:

```bash
# Linux/macOS
echo $DB_USERNAME
echo $MAIL_PASSWORD

# Windows PowerShell
$env:DB_USERNAME
$env:MAIL_PASSWORD
```

## Troubleshooting

### Variables not loading
- Ensure you've exported them: `export VARIABLE_NAME=value`
- Check syntax in .env file
- Use `source .env` to load from file

### Connection errors
- Verify MySQL, Redis, and Kafka are running
- Check connection strings in DB_URL
- Verify port numbers are correct

### Email not sending
- Verify Gmail App Password (not regular password)
- Check 2FA is enabled on Gmail account
- Verify MAIL_USERNAME and MAIL_PASSWORD are correct