# Banking System Core — API Contract (MVP v1)

Base URL: http://localhost:8080

## Conventions
- Currency: stored as decimal string (e.g., "100.00")
- accountNumber: string (unique)
- Idempotency: mutations accept `Idempotency-Key` header (optional in MVP, required in v2)

---

## 1) Create Account
POST /api/v1/accounts

Request
{
  "accountNumber": "ACC1001",
  "ownerName": "Sai Koushik",
  "openingBalance": "1000.00"
}

Response (201)
{
  "id": "uuid",
  "accountNumber": "ACC1001",
  "ownerName": "Sai Koushik",
  "balance": "1000.00",
  "status": "ACTIVE",
  "createdAt": "2026-02-11T10:30:00Z"
}

Errors
- 409 ACCOUNT_EXISTS
- 400 INVALID_INPUT

---

## 2) Get Account
GET /api/v1/accounts/{accountNumber}

Response (200)
{
  "id": "uuid",
  "accountNumber": "ACC1001",
  "ownerName": "Sai Koushik",
  "balance": "1000.00",
  "status": "ACTIVE"
}

Errors
- 404 ACCOUNT_NOT_FOUND

---

## 3) Deposit
POST /api/v1/accounts/{accountNumber}/deposit

Request
{
  "amount": "250.00"
}

Response (200)
{
  "accountNumber": "ACC1001",
  "balance": "1250.00"
}

Errors
- 400 INVALID_AMOUNT
- 404 ACCOUNT_NOT_FOUND

---

## 4) Withdraw
POST /api/v1/accounts/{accountNumber}/withdraw

Request
{
  "amount": "100.00"
}

Response (200)
{
  "accountNumber": "ACC1001",
  "balance": "1150.00"
}

Errors
- 400 INVALID_AMOUNT
- 409 INSUFFICIENT_FUNDS
- 404 ACCOUNT_NOT_FOUND

---

## 5) Transfer (Atomic)
POST /api/v1/transfers

Request
{
  "fromAccountNumber": "ACC1001",
  "toAccountNumber": "ACC2001",
  "amount": "50.00"
}

Response (200)
{
  "transferId": "uuid",
  "fromAccountNumber": "ACC1001",
  "toAccountNumber": "ACC2001",
  "amount": "50.00",
  "status": "COMPLETED"
}

Errors
- 400 INVALID_AMOUNT
- 404 ACCOUNT_NOT_FOUND
- 409 INSUFFICIENT_FUNDS
- 409 SAME_ACCOUNT_NOT_ALLOWED
