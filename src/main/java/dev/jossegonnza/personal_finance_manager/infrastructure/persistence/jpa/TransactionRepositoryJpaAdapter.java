package dev.jossegonnza.personal_finance_manager.infrastructure.persistence.jpa;

import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Money;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TransactionRepositoryJpaAdapter implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;

    public TransactionRepositoryJpaAdapter(TransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public Optional<Transaction> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void save(Transaction transaction) {
        TransactionEntity entity = toEntity(transaction);
        jpaRepository.save(entity);
    }

    @Override
    public List<Transaction> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findAllByAccountId(UUID accountId) {
        return jpaRepository.findByAccountId(accountId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCategoryId(UUID categoryId) {
        return jpaRepository.existsByCategoryId(categoryId);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    //Mappers
    private TransactionEntity toEntity(Transaction transaction) {
        return new TransactionEntity(
                transaction.id(),
                transaction.accountId(),
                transaction.type(),
                transaction.amount().value(),
                transaction.amount().type(),
                transaction.categoryId(),
                transaction.description(),
                transaction.occurredAt()
        );
    }

    private Transaction toDomain(TransactionEntity entity) {
        Money money = new Money(entity.getAmount(), entity.getCurrency());

        return new Transaction(
                entity.getId(),
                entity.getAccountId(),
                entity.getType(),
                money,
                entity.getCategoryId(),
                entity.getDescription(),
                entity.getOccurredAt()
        );
    }
}
