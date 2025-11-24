package dev.jossegonnza.personal_finance_manager.application.port.in.query;

import java.util.UUID;

public interface DeleteTransactionUseCase {
    void deleteById(UUID transactionId);
}
