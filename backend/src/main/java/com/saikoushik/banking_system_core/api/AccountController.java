import com.saikoushik.banking_system_core.api.dto.WithdrawRequest;
package com.saikoushik.banking_system_core.api;

import com.saikoushik.banking_system_core.api.dto.AccountResponse;
import com.saikoushik.banking_system_core.api.dto.CreateAccountRequest;
import com.saikoushik.banking_system_core.api.dto.DepositRequest;
import com.saikoushik.banking_system_core.domain.Account;
import com.saikoushik.banking_system_core.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        Account account = accountService.createAccount(
                request.getAccountNumber(),
                request.getOwnerName(),
                request.getInitialBalance()
        );

        AccountResponse response = new AccountResponse(
                account.getAccountNumber(),
                account.getOwnerName(),
                account.getBalance(),
                account.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable String accountNumber) {

        Account account = accountService.getAccount(accountNumber);

        AccountResponse response = new AccountResponse(
                account.getAccountNumber(),
                account.getOwnerName(),
                account.getBalance(),
                account.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequest request) {

        Account account = accountService.deposit(
                accountNumber,
                request.getAmount()
        );

        AccountResponse response = new AccountResponse(
                account.getAccountNumber(),
                account.getOwnerName(),
                account.getBalance(),
                account.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }
}
