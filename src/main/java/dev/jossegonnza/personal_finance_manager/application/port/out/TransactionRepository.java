package dev.jossegonnza.personal_finance_manager.application.port.out;

import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;

public interface TransactionRepository {
    void save(Transaction transaction);
}
