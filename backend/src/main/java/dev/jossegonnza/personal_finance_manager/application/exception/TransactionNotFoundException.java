package dev.jossegonnza.personal_finance_manager.application.exception;

import java.util.UUID;

public class TransactionNotFoundException extends RuntimeException {
    private final UUID transactionId;

    public TransactionNotFoundException(UUID transactionId) {
        super("Transaction not found: " + transactionId);
        this.transactionId = transactionId;
    }

    public UUID transactionId() {
        return transactionId;
    }
}
