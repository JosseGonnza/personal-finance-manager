package dev.jossegonnza.personal_finance_manager.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Money unit tests")
public class MoneyTest {

    @Test
    void shouldNotAllowCreationWithNegativeValue() {
        //Arrange
        BigDecimal negativeAmount = new BigDecimal("-10.00");

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Money(negativeAmount, CurrencyType.EUR));

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("negative"));
    }

    @Test
    void shouldNotAcceptNullValue() {
        //Arrange
        String currency = "EUR";

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Money(null, CurrencyType.EUR));

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAcceptNullCurrency() {
        //Arrange
        BigDecimal amount = new BigDecimal("10.00");

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Money(amount, null));

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("null"));
    }

    @Test
    void shouldAddMoneyWithSameCurrency() {
        //Arrange
        Money first = new Money(new BigDecimal("10.00"), CurrencyType.EUR);
        Money second = new Money(new BigDecimal("5.00"), CurrencyType.EUR);

        //Act
        Money result = first.plus(second);

        //Assert
        assertEquals(new BigDecimal("15.00"), result.value());
        assertEquals(CurrencyType.EUR, result.type());
    }

    @Test
    void shouldNotAddMoneyWithDifferentCurrency() {
        //Arrange
        Money eur = new Money(new BigDecimal("10.00"), CurrencyType.EUR);
        Money usd = new Money(new BigDecimal("5.00"), CurrencyType.USD);

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eur.plus(usd));

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("currencies"));
    }

    @Test
    void shouldNotAddNullMoney() {
        //Arrange
        Money first = new Money(new BigDecimal("10.00"), CurrencyType.EUR);

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> first.plus(null));

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("null"));
    }

    @Test
    void shouldSubtractMoneyWithSameCurrency() {
        //Arrange
        Money first = new Money(new BigDecimal("20.00"), CurrencyType.EUR);
        Money second = new Money(new BigDecimal("5.00"), CurrencyType.EUR);

        //Act
        Money result = first.minus(second);

        //Assert
        assertEquals(new BigDecimal("15.00"), result.value());
        assertEquals(CurrencyType.EUR, result.type());
    }

    @Test
    void shouldNotSubtractMoneyWithDifferentCurrency() {
        //Arrange
        Money first = new Money(new BigDecimal("20.00"), CurrencyType.EUR);
        Money second = new Money(new BigDecimal("5.00"), CurrencyType.USD);

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> first.minus(second)
        );

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("currencies"));
    }

    @Test
    void shouldNotAllowSubtractionResultingInNegativeValue() {
        //Arrange
        Money first = new Money(new BigDecimal("5.00"), CurrencyType.EUR);
        Money second = new Money(new BigDecimal("10.00"), CurrencyType.EUR);

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> first.minus(second)
        );

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("negative"));
    }

}
