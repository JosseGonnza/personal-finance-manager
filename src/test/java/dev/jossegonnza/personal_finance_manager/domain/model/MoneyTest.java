package dev.jossegonnza.personal_finance_manager.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Money unit tests")
public class MoneyTest {

    @Test
    void shouldNotAllowCreationWithNegativeAmount() {
        //Arrange
        BigDecimal negativeAmount = new BigDecimal("-10.00");
        String currency = "EUR";

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Money(negativeAmount, currency));

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("negative"));
    }

    @Test
    void shouldNotAcceptNullCurrency() {
        //Arrange
        BigDecimal amount = new BigDecimal("10.00");
        String currency = null;

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Money(amount, currency));

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAcceptEmptyCurrency() {
        //Arrange
        BigDecimal amount = new BigDecimal("10.00");
        String currency = "";

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Money(amount, currency));

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("empty"));
    }

    @Test
    void shouldAddMoneyWithSameCurrency() {
        //Arrange
        Money first = new Money(new BigDecimal("10.00"), "EUR");
        Money second = new Money(new BigDecimal("5.00"), "EUR");

        //Act
        Money result = first.plus(second);

        //Assert
        assertEquals(new BigDecimal("15.00"), result.amount());
        assertEquals("EUR", result.currency());
    }

    @Test
    void shouldNotAddMoneyWithDifferentCurrency() {
        //Arrange
        Money eur = new Money(new BigDecimal("10.00"), "EUR");
        Money usd = new Money(new BigDecimal("5.00"), "USD");

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eur.plus(usd));

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("currencies"));
    }
}
