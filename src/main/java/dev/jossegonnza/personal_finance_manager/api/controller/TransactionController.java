package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.CategoryResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.RegisterTransactionRequest;
import dev.jossegonnza.personal_finance_manager.api.dto.TransactionResponse;
import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.RegisterTransactionCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.RegisterTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final RegisterTransactionUseCase registerTransactionUseCase;
    private final GetTransactionUseCase getTransactionUseCase;

    public TransactionController(RegisterTransactionUseCase registerTransactionUseCase, GetTransactionUseCase getTransactionUseCase) {
        this.registerTransactionUseCase = registerTransactionUseCase;
        this.getTransactionUseCase = getTransactionUseCase;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody RegisterTransactionRequest request) throws AccountNotFoundException {
        RegisterTransactionCommand command = new RegisterTransactionCommand(
                request.accountId(),
                request.type(),
                request.amount(),
                request.currency(),
                request.categoryId(),
                request.description(),
                request.occurredAt()
        );

        Transaction transaction = registerTransactionUseCase.register(command);

        return ResponseEntity
                .status(201)
                .body(TransactionResponse.fromDomain(transaction));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable UUID transactionId) {
        Transaction transaction = getTransactionUseCase.getById(transactionId);
        return ResponseEntity
                .ok(TransactionResponse.fromDomain(transaction));
    }
}
