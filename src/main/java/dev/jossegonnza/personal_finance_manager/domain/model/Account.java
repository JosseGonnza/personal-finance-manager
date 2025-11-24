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
    private BigDecimal balance;

    public Account(UUID userId, String name, CurrencyType currencyType) {
        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        this.id = UUID.randomUUID();
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.name = normalizedName;
        this.currencyType = Objects.requireNonNull(currencyType, "currencyType cannot be null");
        this.balance = BigDecimal.ZERO;
    }

    public Transaction registerIncome(Money amount,
                                      UUID categoryId,
                                      String description,
                                      LocalDateTime occurredAt) {
        validateCurrency(amount);
        this.balance = this.balance.add(amount.value());
        return new Transaction(
                this.id,
                TransactionType.INCOME,
                amount,
                categoryId,
                description,
                occurredAt
        );
    }

    public Transaction registerExpense(Money amount,
                                       UUID categoryId,
                                       String description,
                                       LocalDateTime occurredAt) {
        validateCurrency(amount);
        this.balance = this.balance.subtract(amount.value());
        return new Transaction(
                this.id,
                TransactionType.EXPENSE,
                amount,
                categoryId,
                description,
                occurredAt
        );
    }

    public Account updateBalance(BigDecimal newBalance) {
        this.balance = Objects.requireNonNull(newBalance, "balance cannot be null");
        return this;
    }

    private void validateCurrency(Money amount) {
        if (amount.type() != this.currencyType) {
            throw new IllegalArgumentException("transaction currency must match account currency");
        }
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

    public CurrencyType currencyType() {
        return currencyType;
    }

    public BigDecimal balance() {
        return balance;
    }
}
