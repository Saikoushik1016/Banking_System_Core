# Banking System Core

A high-integrity financial ledger engine simulating real-world banking operations — accounts, transfers, deposits, withdrawals, and historical balance tracking.

## Overview

Banking System Core is a production-style Spring Boot backend designed to simulate how real-world banking systems manage money. This project goes beyond basic CRUD — it implements actual banking logic including consistent account balances, audit-friendly transaction workflows, and strict financial validations.

Built following principles used in large-scale fintech platforms: deterministic state updates, strict domain modeling, layered architecture, and full test coverage.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Spring Boot 3 | Application framework |
| Spring Data JPA | Persistence layer |
| PostgreSQL | Production database |
| Docker | Database containerization |
| JUnit 5 + Mockito | Unit testing |
| Maven | Build tool |

---

## Architecture
```
api/          → Controllers + DTOs (HTTP layer)
service/      → Business logic
domain/       → Core entities with behavior
repo/         → Data access layer
```

Key design decisions:
- **Domain factory method** — `Account.open(...)` is the only valid way to create an account
- **DTO pattern** — request/response objects never expose entity internals
- **Rich domain model** — business rules live in the domain, not the service
- **Global exception handler** — clean, consistent error responses

---

## Getting Started

### Prerequisites
- Java 17
- Maven
- Docker Desktop

### 1. Clone the repository
```bash
git clone https://github.com/saikoushik/Banking_System_Core.git
cd Banking_System_Core/backend
```

### 2. Start PostgreSQL via Docker
```bash
docker run --name banking-postgres \
  -e POSTGRES_DB=banking \
  -e POSTGRES_USER=banking_user \
  -e POSTGRES_PASSWORD=banking_pass \
  -p 5432:5432 \
  -d postgres:15
```

### 3. Run the application
```bash
./mvnw spring-boot:run
```

### 4. Run the tests
```bash
./mvnw test
```

---

## API Reference

### Create Account
```
POST /api/v1/accounts
```
```json
{
    "accountNumber": "ACC001",
    "ownerName": "Sai Koushik",
    "initialBalance": 1000.00
}
```

### Get Account
```
GET /api/v1/accounts/{accountNumber}
```

### Deposit
```
POST /api/v1/accounts/{accountNumber}/deposit
```
```json
{
    "amount": 500.00
}
```

### Withdraw
```
POST /api/v1/accounts/{accountNumber}/withdraw
```
```json
{
    "amount": 200.00
}
```

### Transfer
```
POST /api/v1/accounts/{accountNumber}/transfer
```
```json
{
    "toAccountNumber": "ACC002",
    "amount": 300.00
}
```

---

## Error Handling

All errors return consistent JSON responses:
```json
{
    "error": "Account number already exists: ACC001"
}
```

| Status | Scenario |
|---|---|
| 201 Created | Account successfully created |
| 200 OK | Successful operation |
| 400 Bad Request | Validation failure or business rule violation |
| 404 Not Found | Account does not exist |

---

## Test Coverage
```
AccountService — 11 unit tests
✅ createAccount — success
✅ createAccount — duplicate account number
✅ createAccount — negative initial balance
✅ deposit — success
✅ deposit — account not found
✅ withdraw — success
✅ withdraw — insufficient funds
✅ transfer — success
✅ transfer — same account
✅ transfer — source account not found
✅ transfer — destination account not found
```





## Author

**Sai Koushik**
Built as a portfolio project demonstrating enterprise-grade Spring Boot backend development.
