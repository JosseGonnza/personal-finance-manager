package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.AccountResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateAccountRequest;
import dev.jossegonnza.personal_finance_manager.application.port.in.CreateAccountCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.CreateAccountUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private CreateAccountUseCase createAccountUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
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
}
