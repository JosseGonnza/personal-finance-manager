package dev.jossegonnza.personal_finance_manager.application.port.out;

import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository {
    void save(Transaction transaction);
}
