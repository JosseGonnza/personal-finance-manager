package dev.jossegonnza.personal_finance_manager.application.port.in;

import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;

public interface RegisterTransactionUseCase {
    Transaction register(RegisterTransactionCommand command);
}
