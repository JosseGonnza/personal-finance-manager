package dev.jossegonnza.personal_finance_manager.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    void shouldCreateValidExpenseTransaction() {
        //Arrange
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Money amount = new Money(new BigDecimal("50.00"), CurrencyType.EUR);
        String description = "Dinner at restaurant for birthday";
        LocalDateTime occurredAt = LocalDateTime.of(2025, 2, 8, 20, 30);

        //Act
        Transaction transaction = new Transaction(
                accountId,
                TransactionType.EXPENSE,
                amount,
                categoryId,
                description,
                occurredAt
        );

        //Assert
        assertNotNull(transaction.id());
        assertEquals(accountId, transaction.accountId());
        assertEquals(TransactionType.EXPENSE, transaction.type());
        assertEquals(amount, transaction.amount());
        assertEquals(categoryId, transaction.categoryId());
        assertEquals(description, transaction.description());
        assertEquals(occurredAt, transaction.occurredAt());
    }

    @Test
    void shouldNotAllowDescriptionLongerThan200Characters() {
        //Arrange
        UUID accountId = UUID.randomUUID();
        Money amount = new Money(new BigDecimal("10.00"), CurrencyType.EUR);
        String description = "a".repeat(201);
        LocalDateTime occurredAt = LocalDateTime.now();

        //Act
        java.lang.IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Transaction(
                        accountId,
                        TransactionType.EXPENSE,
                        amount,
                        null,
                        description,
                        occurredAt
                )
        );

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("description"));
    }

    @Test
    void shouldNotAcceptNullAccountId() {
        //Arrange
        Money amount = new Money(new BigDecimal("50.00"), CurrencyType.EUR);
        String description = "Dinner at restaurant for birthday";
        LocalDateTime occurredAt = LocalDateTime.of(2025, 2, 8, 20, 30);

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Transaction(
                        null,
                        TransactionType.EXPENSE,
                        amount,
                        null,
                        description,
                        occurredAt
                )
        );

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAcceptNullTransactionType() {
        //Arrange
        UUID accountId = UUID.randomUUID();
        Money amount = new Money(new BigDecimal("50.00"), CurrencyType.EUR);
        String description = "Dinner at restaurant for birthday";
        LocalDateTime occurredAt = LocalDateTime.of(2025, 2, 8, 20, 30);

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Transaction(
                        accountId,
                        null,
                        amount,
                        null,
                        description,
                        occurredAt
                )
        );

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAcceptNullAmount() {
        //Arrange
        UUID accountId = UUID.randomUUID();
        String description = "Dinner at restaurant for birthday";
        LocalDateTime occurredAt = LocalDateTime.now();

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Transaction(
                        accountId,
                        TransactionType.EXPENSE,
                        null,
                        null,
                        description,
                        occurredAt
                )
        );

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAcceptNullOccurredAt() {
        //Arrange
        UUID accountId = UUID.randomUUID();
        Money amount = new Money(new BigDecimal("50.00"), CurrencyType.EUR);
        String description = "Dinner at restaurant for birthday";

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Transaction(
                        accountId,
                        TransactionType.EXPENSE,
                        amount,
                        null,
                        description,
                        null
                )
        );

        //Assert
        assertTrue(exception.getMessage().toLowerCase().contains("null"));
    }
}
