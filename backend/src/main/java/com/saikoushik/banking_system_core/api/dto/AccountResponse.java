package com.saikoushik.banking_system_core.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class AccountResponse {

    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private Instant createdAt;

    public AccountResponse(String accountNumber, String ownerName,
            BigDecimal balance, Instant createdAt) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}