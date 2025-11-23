package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.AccountResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateAccountRequest;
import dev.jossegonnza.personal_finance_manager.api.dto.TransactionResponse;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountTransactionsUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.SearchAccountTransactionsUseCase;
import dev.jossegonnza.personal_finance_manager.application.usecase.query.AccountTransactionsFilter;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final GetAccountTransactionsUseCase getAccountTransactionsUseCase;
    private SearchAccountTransactionsUseCase searchAccountTransactionsUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase,
                             GetAccountUseCase getAccountUseCase,
                             GetAccountTransactionsUseCase getAccountTransactionsUseCase,
                             SearchAccountTransactionsUseCase searchAccountTransactionsUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.getAccountTransactionsUseCase = getAccountTransactionsUseCase;
        this.searchAccountTransactionsUseCase = searchAccountTransactionsUseCase;
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

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable UUID accountId) {
        List<Transaction> transactions = getAccountTransactionsUseCase.getByAccountId(accountId);

        List<TransactionResponse> response = transactions.stream()
                .map(TransactionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/transactions/search")
    public ResponseEntity<List<TransactionResponse>> searchTransactions(
            @PathVariable UUID accountId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to
            ) {
        AccountTransactionsFilter filter = new AccountTransactionsFilter(
                type,
                categoryId,
                from,
                to
        );

        List<Transaction> result = searchAccountTransactionsUseCase.search(accountId, filter);
        List<TransactionResponse> response = result.stream()
                .map(TransactionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }
}
