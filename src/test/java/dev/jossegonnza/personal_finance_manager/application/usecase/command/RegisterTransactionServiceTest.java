package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.RegisterTransactionCommand;
import dev.jossegonnza.personal_finance_manager.domain.model.*;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryAccountRepository;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTransactionServiceTest {

    private InMemoryAccountRepository accountRepository;
    private InMemoryTransactionRepository transactionRepository;
    private RegisterTransactionService service;
    private Account existingAccount;

    @BeforeEach
    void setUp() {
        accountRepository = new InMemoryAccountRepository();
        transactionRepository = new InMemoryTransactionRepository();

        existingAccount = new Account(
                UUID.randomUUID(),
                "Personal",
                CurrencyType.EUR
        );
        accountRepository.save(existingAccount);

        service = new RegisterTransactionService(
                accountRepository,
                transactionRepository
        );
    }

    @Test
    void shouldRegisterIncome() throws AccountNotFoundException {
        //Arrange
        RegisterTransactionCommand command = new RegisterTransactionCommand(
                existingAccount.id(),
                TransactionType.INCOME,
                new BigDecimal("1000.00"),
                CurrencyType.EUR,
                null,
                "Salary",
                LocalDateTime.of(2025, 3, 8, 10, 25)
        );

        //Act
        Transaction transaction = service.register(command);

        //Assert
        assertEquals(existingAccount.id(), transaction.accountId());
        assertEquals(TransactionType.INCOME, transaction.type());
        assertEquals(new Money(new BigDecimal("1000.00"), CurrencyType.EUR), transaction.amount());
        assertEquals("Salary", transaction.description());
        assertEquals(LocalDateTime.of(2025, 3, 8, 10, 25), transaction.occurredAt());
    }

    @Test
    void shouldAddTransactionWhenRegisterIncome() throws AccountNotFoundException {
        //Arrange
        RegisterTransactionCommand command = new RegisterTransactionCommand(
                existingAccount.id(),
                TransactionType.INCOME,
                new BigDecimal("1000.00"),
                CurrencyType.EUR,
                null,
                "Salary",
                LocalDateTime.of(2025, 2, 8, 10, 25)
        );

        //Act
        Transaction transaction = service.register(command);

        //Assert
        assertEquals(1, transactionRepository.findAll().size());
        assertEquals(transaction.id(), transactionRepository.findAll().get(0).id());
    }

    @Test
    void shouldUpdateAccountBalance() throws AccountNotFoundException {
        //Arrange
        RegisterTransactionCommand command = new RegisterTransactionCommand(
                existingAccount.id(),
                TransactionType.INCOME,
                new BigDecimal("1000.00"),
                CurrencyType.EUR,
                null,
                "Salary",
                LocalDateTime.of(2025, 2, 8, 10, 25)
        );

        //Act
        Transaction transaction = service.register(command);

        //Assert
        assertEquals(new Money(new BigDecimal("1000.00"), CurrencyType.EUR),
                accountRepository.findById(command.accountId()).orElseThrow().balance());
    }

    @Test
    void shouldUpdateBalanceWhenRegisterExpense() throws AccountNotFoundException {
        //Arrange
        RegisterTransactionCommand commandIncome = new RegisterTransactionCommand(
                existingAccount.id(),
                TransactionType.INCOME,
                new BigDecimal("1000.00"),
                CurrencyType.EUR,
                null,
                "Salary",
                LocalDateTime.of(2025, 2, 8, 10, 25)
        );

        RegisterTransactionCommand commandExpense = new RegisterTransactionCommand(
                existingAccount.id(),
                TransactionType.EXPENSE,
                new BigDecimal("999.00"),
                CurrencyType.EUR,
                null,
                "New Laptop",
                LocalDateTime.of(2025, 2, 8, 10, 25)
        );
        Transaction transactionIncome = service.register(commandIncome);

        //Act
        Transaction transactionExpense = service.register(commandExpense);

        //Assert
        assertEquals(new Money(new BigDecimal("1.00"), CurrencyType.EUR),
                accountRepository.findById(commandExpense.accountId()).orElseThrow().balance());
        assertEquals(TransactionType.EXPENSE, transactionExpense.type());
        assertEquals(new Money(new BigDecimal("999.00"), CurrencyType.EUR), transactionExpense.amount());
        assertEquals("New Laptop", transactionExpense.description());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        //Arrange
        RegisterTransactionCommand command = new RegisterTransactionCommand(
                UUID.randomUUID(),
                null,
                new BigDecimal("1000.00"),
                CurrencyType.EUR,
                null,
                "Salary",
                LocalDateTime.of(2025, 2, 8, 10, 25)
        );

        //Act
        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> service.register(command)
        );

        //Assert
        assertTrue(exception.getMessage().contains("not found"));
    }
}
