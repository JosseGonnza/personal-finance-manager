package dev.jossegonnza.personal_finance_manager.infrastructure.persistence;

import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final List<Transaction> storage = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        storage.add(transaction);
    }

    public List<Transaction> findAll() {
        return Collections.unmodifiableList(storage);
    }
}
