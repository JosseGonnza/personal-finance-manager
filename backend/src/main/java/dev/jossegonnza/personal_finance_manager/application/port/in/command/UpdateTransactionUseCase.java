package dev.jossegonnza.personal_finance_manager.application.port.in.command;

import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;

import java.util.UUID;

public interface UpdateTransactionUseCase {
    Transaction update(UUID transactionId, UpdateTransactionCommand command);
}
