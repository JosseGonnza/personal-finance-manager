package dev.jossegonnza.personal_finance_manager.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Test
    void shouldIncreaseBalanceWhenRegisterIncome() {
        //Arrange
        CurrencyType currency = CurrencyType.EUR;
        String name = "Personal";
        UUID userId = UUID.randomUUID();
        Account account = new Account(userId, name, currency);
        Money incomeAmount = new Money(new BigDecimal("10.00"), currency);

        //Act
        Transaction income = account.registerIncome(
                incomeAmount,
                null,
                "Propina",
                LocalDateTime.of(2025, 2, 8, 10, 10));

        //Assert
        assertEquals(new Money(new BigDecimal("10.00"), CurrencyType.EUR), account.balance());
        assertEquals(account.id(), income.accountId());
        assertEquals(TransactionType.INCOME, income.type());
        assertEquals(incomeAmount, income.amount());
    }

    @Test
    void shouldDecreaseBalanceWhenRegisterExpense() {
        //Arrange
        CurrencyType currency = CurrencyType.EUR;
        String name = "Personal";
        UUID userId = UUID.randomUUID();
        Account account = new Account(userId, name, currency);
        Money incomeAmount = new Money(new BigDecimal("100.00"), currency);
        Money expenseAmount = new Money(new BigDecimal("10.00"), currency);
        Transaction income = account.registerIncome(
                incomeAmount,
                null,
                "Propina",
                LocalDateTime.of(2025, 2, 8, 10, 10));

        //Act
        Transaction expense = account.registerExpense(
                expenseAmount,
                null,
                "Gasoil",
                LocalDateTime.of(2025, 2, 8, 10, 10));

        //Assert
        assertEquals(new Money(new BigDecimal("90.00"), CurrencyType.EUR), account.balance());
        assertEquals(account.id(), expense.accountId());
        assertEquals(TransactionType.EXPENSE, expense.type());
        assertEquals(expenseAmount, expense.amount());
    }

    @Test
    void shouldNotRegisterIncomeWithNullAmount() {
        // Arrange
        Account account = new Account(UUID.randomUUID(), "Personal", CurrencyType.EUR);

        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> account.registerIncome(
                        null,
                        null,
                        "Propina",
                        LocalDateTime.of(2025, 2, 8, 10, 10)
                )
        );

        // Assert
        assertTrue(exception.getMessage().toLowerCase().contains("amount"));
    }

    @Test
    void shouldNotAddMoneyWithDifferentCurrencyWhenRegisterIncome() {
        //Arrange
        String name = "Personal";
        UUID userId = UUID.randomUUID();
        Account account = new Account(userId, name, CurrencyType.EUR);
        Money incomeAmount = new Money(new BigDecimal("10.00"), CurrencyType.USD);

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> account.registerIncome(
                        incomeAmount,
                        null,
                        "Propina",
                        LocalDateTime.of(2025, 2, 8, 10, 10))
        );

        //Assert
        assertTrue(exception.getMessage().contains("match"));
    }

    @Test
    void shouldNotSubtractMoneyWithDifferentCurrencyWhenRegisterIncome() {
        //Arrange
        String name = "Personal";
        UUID userId = UUID.randomUUID();
        Account account = new Account(userId, name, CurrencyType.EUR);
        Money incomeAmount = new Money(new BigDecimal("10.00"), CurrencyType.EUR);
        Money expenseAmount = new Money(new BigDecimal("10.00"), CurrencyType.USD);

        Transaction income = account.registerIncome(
                incomeAmount,
                null,
                "Propina",
                LocalDateTime.of(2025, 2, 8, 10, 10));

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> account.registerExpense(
                        expenseAmount,
                        null,
                        "Propina",
                        LocalDateTime.of(2025, 2, 8, 10, 10))
        );

        //Assert
        assertTrue(exception.getMessage().contains("match"));
    }
}
