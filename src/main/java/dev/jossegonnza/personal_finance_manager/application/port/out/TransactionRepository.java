package dev.jossegonnza.personal_finance_manager.application.port.out;

import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository {
    Optional<Transaction> findById(UUID transactionId);
    void save(Transaction transaction);
}
