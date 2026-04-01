package com.saikoushik.banking_system_core.service;
import org.springframework.transaction.annotation.Transactional;

import com.saikoushik.banking_system_core.domain.Account;
import com.saikoushik.banking_system_core.repo.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));
    }

    public Account createAccount(String accountNumber, String ownerName, BigDecimal initialBalance) {

        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        accountRepository.findByAccountNumber(accountNumber)
                .ifPresent(a -> {
                    throw new IllegalArgumentException("Account number already exists: " + accountNumber);
                });

        Account account = Account.open(accountNumber, ownerName, initialBalance);
        return accountRepository.save(account);
    }
    public Account deposit(String accountNumber, BigDecimal amount) {

    Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Account not found: " + accountNumber));

    account.deposit(amount);

    return accountRepository.save(account);
}
public Account withdraw(String accountNumber, BigDecimal amount) {

    Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Account not found: " + accountNumber));

    account.withdraw(amount);

    return accountRepository.save(account);
}
@Transactional
public Account transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {

    if (fromAccountNumber.equals(toAccountNumber)) {
        throw new IllegalArgumentException(
                "Cannot transfer to the same account");
    }

    Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Source account not found: " + fromAccountNumber));

    Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Destination account not found: " + toAccountNumber));

    fromAccount.withdraw(amount);
    toAccount.deposit(amount);

    accountRepository.save(fromAccount);
    accountRepository.save(toAccount);

    return fromAccount;
}
}