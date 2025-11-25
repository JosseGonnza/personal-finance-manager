package dev.jossegonnza.personal_finance_manager.application.port.in.command;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;

public interface RegisterTransactionUseCase {
    Transaction register(RegisterTransactionCommand command) throws AccountNotFoundException;
}
