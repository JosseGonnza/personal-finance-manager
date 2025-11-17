package dev.jossegonnza.personal_finance_manager.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("currency cannot be null or empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("money amount cannot be null or negative");
        }
        this.amount = amount;
        this.currency = currency.trim().toUpperCase();
    }

    public Money plus(Money other) {
        Objects.requireNonNull(other, "money to add cannot be null");
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("currencies must match");
        }
        BigDecimal newAmount = this.amount.add(other.amount);
        return new Money(newAmount, this.currency);
    }

    public BigDecimal amount() {
        return amount;
    }

    public String currency() {
        return currency;
    }
}
