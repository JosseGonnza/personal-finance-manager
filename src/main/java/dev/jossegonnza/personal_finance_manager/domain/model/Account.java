package dev.jossegonnza.personal_finance_manager.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final UUID userId;
    private final String name;
    private final CurrencyType currencyType;
    private final Money balance;

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
