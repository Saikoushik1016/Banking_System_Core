package com.saikoushik.banking_system_core;

import com.saikoushik.banking_system_core.domain.Account;
import com.saikoushik.banking_system_core.repo.AccountRepository;
import com.saikoushik.banking_system_core.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account existingAccount;

    @BeforeEach
    void setUp() {
        existingAccount = Account.open("ACC001", "Sai Koushik",
                new BigDecimal("1000.00"));
    }

    @Test
    void createAccount_success() {
        when(accountRepository.findByAccountNumber("ACC002"))
                .thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(i -> i.getArgument(0));

        Account result = accountService.createAccount(
                "ACC002", "New User", new BigDecimal("500.00"));

        assertNotNull(result);
        assertEquals("ACC002", result.getAccountNumber());
        assertEquals("New User", result.getOwnerName());
        assertEquals(new BigDecimal("500.00"), result.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_duplicateAccountNumber_throwsException() {
        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(Optional.of(existingAccount));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.createAccount(
                        "ACC001", "Someone", new BigDecimal("100.00")));

        assertEquals("Account number already exists: ACC001", ex.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void createAccount_negativeInitialBalance_throwsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.createAccount(
                        "ACC003", "Someone", new BigDecimal("-100.00")));

        assertEquals("Initial balance cannot be negative", ex.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deposit_success() {
        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(i -> i.getArgument(0));

        Account result = accountService.deposit(
                "ACC001", new BigDecimal("500.00"));

        assertEquals(new BigDecimal("1500.00"), result.getBalance());
        verify(accountRepository, times(1)).save(existingAccount);
    }

    @Test
    void deposit_accountNotFound_throwsException() {
        when(accountRepository.findByAccountNumber("ACC999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.deposit(
                        "ACC999", new BigDecimal("100.00")));

        assertEquals("Account not found: ACC999", ex.getMessage());
    }

    @Test
    void withdraw_success() {
        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(i -> i.getArgument(0));

        Account result = accountService.withdraw(
                "ACC001", new BigDecimal("300.00"));

        assertEquals(new BigDecimal("700.00"), result.getBalance());
        verify(accountRepository, times(1)).save(existingAccount);
    }

    @Test
    void withdraw_insufficientFunds_throwsException() {
        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(Optional.of(existingAccount));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.withdraw(
                        "ACC001", new BigDecimal("9999.00")));

        assertEquals("insufficient funds", ex.getMessage());
    }

    @Test
    void transfer_success() {
        Account toAccount = Account.open("ACC002", "Test User",
                new BigDecimal("500.00"));

        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(Optional.of(existingAccount));
        when(accountRepository.findByAccountNumber("ACC002"))
                .thenReturn(Optional.of(toAccount));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(i -> i.getArgument(0));

        Account result = accountService.transfer(
                "ACC001", "ACC002", new BigDecimal("200.00"));

        assertEquals(new BigDecimal("800.00"), result.getBalance());
        assertEquals(new BigDecimal("700.00"), toAccount.getBalance());
    }

    @Test
    void transfer_sameAccount_throwsException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.transfer(
                        "ACC001", "ACC001", new BigDecimal("100.00")));

        assertEquals("Cannot transfer to the same account", ex.getMessage());
    }

    @Test
    void transfer_sourceNotFound_throwsException() {
        when(accountRepository.findByAccountNumber("ACC999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.transfer(
                        "ACC999", "ACC001", new BigDecimal("100.00")));

        assertEquals("Source account not found: ACC999", ex.getMessage());
    }

    @Test
    void transfer_destinationNotFound_throwsException() {
        when(accountRepository.findByAccountNumber("ACC001"))
                .thenReturn(Optional.of(existingAccount));
        when(accountRepository.findByAccountNumber("ACC999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.transfer(
                        "ACC001", "ACC999", new BigDecimal("100.00")));

        assertEquals("Destination account not found: ACC999", ex.getMessage());
    }
}