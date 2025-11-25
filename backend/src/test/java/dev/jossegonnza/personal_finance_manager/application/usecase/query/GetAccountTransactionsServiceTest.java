package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountTransactionsUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.*;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryAccountRepository;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryTransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetAccountTransactionsServiceTest {

    @Test
    void shouldReturnTransactionsForAccountWhenAccountHasTransactions() {
        //Arrange
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        GetAccountTransactionsUseCase service = new GetAccountTransactionsService(
                accountRepository, transactionRepository
        );
        Account account = new Account(
                UUID.randomUUID(),
                "Personal",
                CurrencyType.EUR
        );
        accountRepository.save(account);
        UUID accountId = account.id();
        Transaction t1 = new Transaction(
                accountId,
                TransactionType.INCOME,
                new Money(new BigDecimal("50.00"), CurrencyType.EUR),
                UUID.randomUUID(),
                "Extras",
                LocalDateTime.of(2025, 2, 8, 10,30)
        );
        Transaction t2 = new Transaction(
                accountId,
                TransactionType.EXPENSE,
                new Money(new BigDecimal("20.00"), CurrencyType.EUR),
                UUID.randomUUID(),
                "Romantic Dinner",
                LocalDateTime.of(2025, 2, 8, 10,30)
        );
        Transaction t3 = new Transaction(
                UUID.randomUUID(),  //Otra cuenta
                TransactionType.INCOME,
                new Money(new BigDecimal("1000.00"), CurrencyType.EUR),
                UUID.randomUUID(),
                "Salary",
                LocalDateTime.of(2025, 2, 8, 10,30)
        );
        transactionRepository.save(t1);
        transactionRepository.save(t2);
        transactionRepository.save(t3);

        //Act
        List<Transaction> result = service.getByAccountId(accountId);

        //Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertTrue(result.contains(t2));
        assertFalse(result.contains(t3));
    }

    @Test
    void shouldReturnEmptyListWhenAccountHasNoTransactions() {
        // Arrange
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        GetAccountTransactionsUseCase service = new GetAccountTransactionsService(
                accountRepository, transactionRepository
        );

        Account account = new Account(
                UUID.randomUUID(),
                "Personal",
                CurrencyType.EUR
        );
        accountRepository.save(account);

        // Act
        List<Transaction> result = service.getByAccountId(account.id());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenAccountDoesNotExist() {
        //Arrange
        UUID accountId = UUID.randomUUID();
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        GetAccountTransactionsUseCase service = new GetAccountTransactionsService(
                accountRepository, transactionRepository
        );

        UUID unknownId = UUID.randomUUID();

        // Act + Assert
        AccountNotFoundException ex = assertThrows(
                AccountNotFoundException.class,
                () -> service.getByAccountId(unknownId)
        );

        assertEquals("Account not found: " + unknownId, ex.getMessage());
    }
}
