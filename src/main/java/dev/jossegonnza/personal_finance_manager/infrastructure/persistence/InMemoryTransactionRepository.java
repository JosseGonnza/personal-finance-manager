package dev.jossegonnza.personal_finance_manager.infrastructure.persistence;

import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<UUID, Transaction> storage = new HashMap<>();


    @Override
    public Optional<Transaction> findById(UUID transactionId) {
        return Optional.ofNullable(storage.get(transactionId));
    }

    @Override
    public void save(Transaction transaction) {
        storage.put(transaction.id(), transaction);
    }

    public List<Transaction> findAll() {
        return List.copyOf(storage.values());
    }
}
