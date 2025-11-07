# JBDL9_E-Wallet

1. Onboarding Curl:
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
