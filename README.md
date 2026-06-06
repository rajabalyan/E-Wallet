# JBDL9_E-Wallet

# Onboarding Curl:
curl --location 'http://localhost:8081/onboarding-service/create/user' \
--header 'Content-Type: application/json' \
--header 'Cookie: Cookie_1=value' \
--data-raw '{
    "name": "Aakash",
    "email": "robinsingh1712@gmail.com",
    "password": "123456",
    "mobileNo": "8890237687",
    "dob": "14/08/1997",
    "userIdentifier": "AADHAAR_CARD",
    "userIdentifierValue": "7373673636367222"
}'

# Validate OTP Curl
curl --location 'http://localhost:8081/onboarding-service/validate/otp' \
--header 'Content-Type: application/json' \
--header 'Cookie: Cookie_1=value' \
--data-raw '{
    "otp": "320119",
    "email": "robinsingh1712@gmail.com"
}'

# User Login Curl:
curl --location 'http://localhost:8081/onboarding-service/user/login' \
--header 'Content-Type: application/json' \
--header 'Cookie: Cookie_1=value' \
--data '{
    "username": "8890237687",
    "password": "123456"
}'

# Initiate Transaction Curl:
curl --location 'http://localhost:8084/txn-service/initiate/transaction' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5NzE3ODAzNDc5IiwiaWF0IjoxNzYxMzc2MTQ2LCJleHAiOjE3NjEzNzY3NDYsInJvbGUiOiJOT1JNQUwifQ.P92JToVsxcdu_qMKVKGwZIAuN76Rp_5kx174XYTkhr4' \
--header 'Cookie: Cookie_1=value' \
--data '{
    "amount": 1100.0,
    "purpose": "Dummy Transfer",
    "receiver": "8890237687"
}'

# Transaction History Curl
curl --location 'http://localhost:8084/txn-service/get/transaction/history' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4ODkwMjM3Njg3IiwiaWF0IjoxNzYxMzc3MzQxLCJleHAiOjE3NjEzNzc5NDEsInJvbGUiOiJOT1JNQUwifQ.we23w_H7CyyrRrXTEvbF7CJMGtluCeJ3zzsUmWwfKFc' \
--header 'Cookie: Cookie_1=value'

# Wallet Balance API Curl:
curl --location 'http://localhost:8083/wallet-service/get/balance' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4ODkwMjM3Njg3IiwiaWF0IjoxNzYxMzc4MDIwLCJleHAiOjE3NjEzNzg2MjAsInJvbGUiOiJOT1JNQUwifQ.-1K_uIECwq803o4S8TtY4pwGuH-AEwSI273kSmOtp3M' \
--header 'Cookie: Cookie_1=value'

## Frontend UI

A simple browser UI is available in `frontend/index.html`.
Open that file in your browser after starting the backend services:

1. Start `OnboardingService` on port `8081`
2. Start `WalletService` on port `8083`
3. Start `TransactionService` on port `8084`
4. Open `frontend/index.html`

If you use VS Code, a Live Server extension will make it easier to open the page with a local URL.
