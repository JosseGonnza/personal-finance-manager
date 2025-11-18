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
        if (!this.currencyType.equals(other.currencyType)) {
            throw new IllegalArgumentException("currencies must match");
        }
        BigDecimal newAmount = this.amount.add(other.amount);
        return new Money(newAmount, this.currencyType);
    }

    public BigDecimal amount() {
        return amount;
    }

    public CurrencyType type() {
        return currencyType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && currencyType == money.currencyType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currencyType);
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currencyType=" + currencyType +
                '}';
    }
}
