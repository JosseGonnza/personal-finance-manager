package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.exception.TransactionNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.RegisterTransactionCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.DeleteTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryAccountRepository;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteTransactionServiceTest {

    private InMemoryAccountRepository accountRepository;
    private InMemoryTransactionRepository transactionRepository;
    private RegisterTransactionService registerTransactionService;
    private DeleteTransactionUseCase service;

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

        registerTransactionService = new RegisterTransactionService(
                accountRepository,
                transactionRepository
        );

        service = new DeleteTransactionService(
                transactionRepository,
                accountRepository
        );
    }

    @Test
    void shouldDeleteIncomeTransactionAndRevertBalance() {
        // Arrange: balance pasa de 0 a +100
        var tx = registerTransactionService.register(
                new RegisterTransactionCommand(
                        existingAccount.id(),
                        TransactionType.INCOME,
                        new BigDecimal("100.00"),
                        CurrencyType.EUR,
                        null,
                        "Salary",
                        LocalDateTime.of(2025, 3, 1, 10, 0)
                )
        );

        assertEquals(
                new BigDecimal("100.00"),
                accountRepository.findById(existingAccount.id()).orElseThrow().balance()
        );

        // Act
        service.deleteById(tx.id());

        // Assert: balance vuelve a 0 y la tx desaparece
        BigDecimal currentBalance = accountRepository
                .findById(existingAccount.id())
                .orElseThrow()
                .balance();

        assertEquals(
                new BigDecimal("0.00"),
                currentBalance
        );
        assertTrue(transactionRepository.findById(tx.id()).isEmpty());
    }

    @Test
    void shouldDeleteExpenseTransactionAndRevertBalance() {
        // Arrange: primero un ingreso de 200
        registerTransactionService.register(
                new RegisterTransactionCommand(
                        existingAccount.id(),
                        TransactionType.INCOME,
                        new BigDecimal("200.00"),
                        CurrencyType.EUR,
                        null,
                        "Salary",
                        LocalDateTime.of(2025, 3, 1, 10, 0)
                )
        );

        // Luego un gasto de 50 → balance 150
        var expenseTx = registerTransactionService.register(
                new RegisterTransactionCommand(
                        existingAccount.id(),
                        TransactionType.EXPENSE,
                        new BigDecimal("50.00"),
                        CurrencyType.EUR,
                        null,
                        "Groceries",
                        LocalDateTime.of(2025, 3, 2, 12, 0)
                )
        );

        assertEquals(
                new BigDecimal("150.00"),
                accountRepository.findById(existingAccount.id()).orElseThrow().balance()
        );

        // Act: borramos el gasto → el balance debería volver a 200
        service.deleteById(expenseTx.id());

        // Assert
        BigDecimal currentBalance = accountRepository
                .findById(existingAccount.id())
                .orElseThrow()
                .balance();

        assertEquals(
                new BigDecimal("200.00"),
                currentBalance
        );
        assertTrue(transactionRepository.findById(expenseTx.id()).isEmpty());
    }

    @Test
    void shouldThrowWhenTransactionNotFound() {
        // Arrange
        UUID unknownId = UUID.randomUUID();

        // Act + Assert
        assertThrows(
                TransactionNotFoundException.class,
                () -> service.deleteById(unknownId)
        );
    }
}