package dev.jossegonnza.personal_finance_manager.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;
    private final CurrencyType currencyType;

    public Money(BigDecimal amount, CurrencyType currencyType) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("money amount cannot be null or negative");
        }
        this.amount = amount;
        this.currencyType = Objects.requireNonNull(currencyType, "currencyType cannot null");
    }

    public Money plus(Money other) {
        Objects.requireNonNull(other, "money to add cannot be null");
        validateCurrency(other);
        BigDecimal newAmount = this.amount.add(other.amount);
        return new Money(newAmount, this.currencyType);
    }

    public Money minus(Money other) {
        Objects.requireNonNull(other, "money to subtract cannot be null");
        validateCurrency(other);
        BigDecimal newAmount = this.amount.subtract(other.amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("resulting money amount cannot be negative");
        }
        return new Money(newAmount, this.currencyType);
    }

    public BigDecimal value() {
        return amount;
    }

    public CurrencyType type() {
        return currencyType;
    }

    private void validateCurrency(Money other) {
        if (!this.currencyType.equals(other.currencyType)) {
            throw new IllegalArgumentException("currencies must match");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.compareTo(money.amount) == 0 &&
                currencyType == money.currencyType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currencyType);
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currencyType=" + currencyType +
                '}';
    }
}
