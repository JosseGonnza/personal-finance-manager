package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.exception.TransactionNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.RegisterTransactionCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.UpdateTransactionCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.UpdateTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.*;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryAccountRepository;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdateTransactionServiceTest {
    private InMemoryAccountRepository accountRepository;
    private InMemoryTransactionRepository transactionRepository;
    private RegisterTransactionService registerTransactionService;
    private UpdateTransactionUseCase service;

    private Account existingAccount;
    private Transaction existingTransaction;

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

        registerTransactionService = new RegisterTransactionService(
                accountRepository,
                transactionRepository
        );

        existingTransaction = registerTransactionService.register(
                new RegisterTransactionCommand(
                        existingAccount.id(),
                        TransactionType.INCOME,
                        new BigDecimal("100.00"),
                        CurrencyType.EUR,
                        null,
                        "Initial salary",
                        LocalDateTime.of(2025, 3, 1, 10, 0)
                )
        );

        service = new UpdateTransactionService(
                accountRepository,
                transactionRepository
        );
    }

    @Test
    void shouldUpdateNonMonetaryFieldsWithoutChangingBalance() {
        // Arrange
        UpdateTransactionCommand command = new UpdateTransactionCommand(
                existingTransaction.type(),
                existingTransaction.amount().value(),
                existingTransaction.amount().type(),
                UUID.randomUUID(),
                "Updated description",
                LocalDateTime.of(2025, 3, 2, 12, 0)
        );

        BigDecimal previousBalance = accountRepository
                .findById(existingAccount.id())
                .orElseThrow()
                .balance();

        // Act
        Transaction updatedTransaction = service.update(existingTransaction.id(), command);

        // Assert
        assertEquals(existingTransaction.id(), updatedTransaction.id());
        assertEquals(command.type(), updatedTransaction.type());
        assertEquals(new Money(command.amount(), command.currency()), updatedTransaction.amount());
        assertEquals(command.categoryId(), updatedTransaction.categoryId());
        assertEquals(command.description(), updatedTransaction.description());
        assertEquals(command.occurredAt(), updatedTransaction.occurredAt());

        BigDecimal currentBalance = accountRepository
                .findById(existingAccount.id())
                .orElseThrow()
                .balance();

        assertEquals(previousBalance, currentBalance);
    }

    @Test
    void shouldRecalculateBalanceWhenAmountChangesKeepingType() {
        // Arrange
        UpdateTransactionCommand command = new UpdateTransactionCommand(
                TransactionType.INCOME,
                new BigDecimal("150.00"),
                CurrencyType.EUR,
                existingTransaction.categoryId(),
                existingTransaction.description(),
                existingTransaction.occurredAt()
        );

        // Act
        Transaction updated = service.update(existingTransaction.id(), command);

        // Assert
        assertEquals(new Money(new BigDecimal("150.00"), CurrencyType.EUR), updated.amount());

        BigDecimal currentBalance = accountRepository
                .findById(existingAccount.id())
                .orElseThrow()
                .balance();

        assertEquals(new BigDecimal("150.00"), currentBalance);
    }

    @Test
    void shouldRecalculateBalanceWhenTypeChangesFromIncomeToExpense() {
        // Arrange
        UpdateTransactionCommand command = new UpdateTransactionCommand(
                TransactionType.EXPENSE,
                new BigDecimal("40.00"),
                CurrencyType.EUR,
                existingTransaction.categoryId(),
                "Changed to expense",
                existingTransaction.occurredAt()
        );

        // Act
        Transaction updated = service.update(existingTransaction.id(), command);

        // Assert
        assertEquals(TransactionType.EXPENSE, updated.type());
        assertEquals(new Money(new BigDecimal("40.00"), CurrencyType.EUR), updated.amount());

        BigDecimal currentBalance = accountRepository
                .findById(existingAccount.id())
                .orElseThrow()
                .balance();

        assertEquals(new BigDecimal("-40.00"), currentBalance);
    }

    @Test
    void shouldThrowWhenTransactionNotFound() {
        // Arrange
        UUID unknownId = UUID.randomUUID();
        UpdateTransactionCommand command = new UpdateTransactionCommand(
                TransactionType.EXPENSE,
                new BigDecimal("10.00"),
                CurrencyType.EUR,
                null,
                "Whatever",
                LocalDateTime.of(2025, 3, 1, 10, 0)
        );

        // Act + Assert
        assertThrows(
                TransactionNotFoundException.class,
                () -> service.update(unknownId, command)
        );
    }
}
