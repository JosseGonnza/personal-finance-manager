package dev.jossegonnza.personal_finance_manager.application.port.in.query;

import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;

import java.util.UUID;

public interface GetTransactionUseCase {
    Transaction getById(UUID transactionId);
}
