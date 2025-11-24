package dev.jossegonnza.personal_finance_manager.application.port.out;

import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Optional<Transaction> findById(UUID transactionId);
    void save(Transaction transaction);
    List<Transaction> findAll();
    List<Transaction> findAllByAccountId(UUID accountId);
    boolean existsByCategoryId(UUID categoryId);
    void deleteById(UUID transactionId);
}
