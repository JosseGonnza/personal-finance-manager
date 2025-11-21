package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.RegisterTransactionRequest;
import dev.jossegonnza.personal_finance_manager.api.dto.TransactionResponse;
import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.RegisterTransactionCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.RegisterTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final RegisterTransactionUseCase registerTransactionUseCase;

    public TransactionController(RegisterTransactionUseCase registerTransactionUseCase) {
        this.registerTransactionUseCase = registerTransactionUseCase;
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
}
