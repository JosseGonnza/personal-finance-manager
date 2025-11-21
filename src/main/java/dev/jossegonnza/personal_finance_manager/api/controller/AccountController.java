package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.AccountResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateAccountRequest;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase, GetAccountUseCase getAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
        CreateAccountCommand command = new CreateAccountCommand(
                request.userId(),
                request.name(),
                request.currency()
        );

        Account account = createAccountUseCase.create(command);

        return ResponseEntity
                .status(201)
                .body(AccountResponse.fromDomain(account));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getById(@PathVariable UUID accountId) {
        Account account = getAccountUseCase.getById(accountId);
        return ResponseEntity
                .ok(AccountResponse.fromDomain(account));
    }
}
