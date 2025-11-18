package dev.jossegonnza.personal_finance_manager.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final UUID userId;
    private final String name;
    private final CurrencyType currencyType;
    private Money balance;

    public Account(UUID userId, String name, CurrencyType currencyType) {
        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()){
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        this.id = UUID.randomUUID();
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.name = normalizedName;
        this.currencyType = Objects.requireNonNull(currencyType, "currencyType cannot be null");
        this.balance = new Money(new BigDecimal("0.00"), currencyType);;
    }

    public Transaction registerIncome(Money amount,UUID categoryId, String description, LocalDateTime occurredAt) {
        if (amount.type() != this.currencyType){
            throw new IllegalArgumentException("transaction currency must match account currency");
        }
        this.balance = this.balance.plus(amount);
        return new Transaction(
                this.id,
                TransactionType.INCOME,
                amount,
                categoryId,
                description,
                occurredAt);
    }

    public Transaction registerExpense(Money amount,UUID categoryId, String description, LocalDateTime occurredAt) {
        if (amount.type() != this.currencyType){
            throw new IllegalArgumentException("transaction currency must match account currency");
        }
        this.balance = this.balance.minus(amount);
        return new Transaction(
                this.id,
                TransactionType.EXPENSE,
                amount,
                categoryId,
                description,
                occurredAt);
    }

    public UUID id() {
        return id;
    }

    public UUID userId() {
        return userId;
    }

    public String name() {
        return name;
    }

    public CurrencyType type() {
        return currencyType;
    }

    public Money balance() {
        return balance;
    }
}
