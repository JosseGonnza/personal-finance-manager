package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.port.in.query.SearchAccountTransactionsUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.*;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryAccountRepository;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryTransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SearchAccountTransactionsServiceTest {
    @Test
    void shouldFilterTransactionByType() {
        //Arrange
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        SearchAccountTransactionsUseCase service = new SearchAccountTransactionsService(
                accountRepository,
                transactionRepository);

        UUID userId = UUID.randomUUID();
        Account account = new Account(userId, "Personal", CurrencyType.EUR);
        accountRepository.save(account);

        Transaction t1 = new Transaction(
                account.id(),
                TransactionType.INCOME,
                new Money(new BigDecimal("100.00"), CurrencyType.EUR),
                UUID.randomUUID(),
                "Salary",
                LocalDateTime.now()
        );

        Transaction t2 = new Transaction(
                account.id(),
                TransactionType.EXPENSE,
                new Money(new BigDecimal("20.00"), CurrencyType.EUR),
                UUID.randomUUID(),
                "Coffee",
                LocalDateTime.now()
        );

        Transaction t3 = new Transaction(
                account.id(),
                TransactionType.INCOME,
                new Money(new BigDecimal("30.00"), CurrencyType.EUR),
                UUID.randomUUID(),
                "Gift",
                LocalDateTime.now()
        );
        transactionRepository.save(t1);
        transactionRepository.save(t2);
        transactionRepository.save(t3);

        AccountTransactionsFilter filter = new AccountTransactionsFilter(
                TransactionType.INCOME,
                null,
                null,
                null
        );

        //Act
        List<Transaction> result = service.search(account.id(), filter);

        //Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(t1));
        assertFalse(result.contains(t2));
        assertTrue(result.contains(t3));
    }
}
