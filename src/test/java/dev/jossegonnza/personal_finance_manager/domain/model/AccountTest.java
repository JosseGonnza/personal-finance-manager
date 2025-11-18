package dev.jossegonnza.personal_finance_manager.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    @Test
    void shouldCreateValidAccountWithBalance0() {
        //Arrange
        UUID userId = UUID.randomUUID();
        String name = "Personal";
        CurrencyType currency = CurrencyType.EUR;

        //Act
        Account account = new Account(
                userId,
                name,
                currency
        );

        //Assert
        assertNotNull(account.id());
        assertEquals(userId, account.userId());
        assertEquals(name, account.name());
        assertEquals(CurrencyType.EUR, account.type());
        assertEquals(new Money(new BigDecimal("0.00"), currency), account.balance());
    }

    @Test
    void shouldNotAcceptNullUserId() {
        //Arrange
        String name = "Personal";
        CurrencyType currency = CurrencyType.EUR;

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Account(
                        null,
                        name,
                        currency
                )
        );

        //Assert
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    void shouldNotAcceptNullName() {
        //Arrange
        UUID userId = UUID.randomUUID();
        CurrencyType currency = CurrencyType.EUR;

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Account(
                        userId,
                        null,
                        currency
                )
        );

        //Assert
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    void shouldNotAcceptEmptyName() {
        //Arrange
        UUID userId = UUID.randomUUID();
        CurrencyType currency = CurrencyType.EUR;

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Account(
                        userId,
                        "",
                        currency
                )
        );

        //Assert
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    void shouldNotAcceptNullCurrencyType() {
        //Arrange
        UUID userId = UUID.randomUUID();
        String name = "Personal";

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Account(
                        userId,
                        name,
                        null
                )
        );

        //Assert
        assertTrue(exception.getMessage().contains("null"));
    }
}
