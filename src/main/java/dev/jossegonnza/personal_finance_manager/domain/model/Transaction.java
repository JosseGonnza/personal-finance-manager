package dev.jossegonnza.personal_finance_manager.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final UUID id;
    private final UUID accountId;
    private final TransactionType transactionType;
    private final Money amount;
    private final UUID categoryId;
    private final String description;
    private final LocalDateTime occurredAt;

    public Transaction(UUID accountId, TransactionType transactionType, Money amount, UUID categoryId,
                       String description, LocalDateTime occurredAt) {
        String normalizedDescription = (description == null) ? "" : description;
        if (normalizedDescription.length() > 200) {
            throw new IllegalArgumentException("description cannot be longer than 200 characters");
        }
        this.id = UUID.randomUUID();
        this.accountId = Objects.requireNonNull(accountId, "accountId cannot be null");
        this.transactionType = Objects.requireNonNull(transactionType, "transactionType cannot be null");
        this.amount = Objects.requireNonNull(amount, "amount cannot be null");
        this.categoryId = categoryId;
        this.description = normalizedDescription;
        this.occurredAt = Objects.requireNonNull(occurredAt,"occurredAt cannot be null");
    }

    public Transaction(UUID id,
                       UUID accountId,
                       TransactionType type,
                       Money amount,
                       UUID categoryId,
                       String description,
                       LocalDateTime occurredAt) {
        this.id = id;
        this.accountId = accountId;
        this.transactionType = type;
        this.amount = amount;
        this.categoryId = categoryId;
        this.description = description;
        this.occurredAt = occurredAt;
    }


    public UUID id() {
        return id;
    }

    public UUID accountId() {
        return accountId;
    }

    public TransactionType type() {
        return transactionType;
    }

    public Money amount() {
        return amount;
    }

    public UUID categoryId() {
        return categoryId;
    }

    public String description() {
        return description;
    }

    public LocalDateTime occurredAt() {
        return occurredAt;
    }
}
