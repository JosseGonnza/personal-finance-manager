package dev.jossegonnza.personal_finance_manager.application.usecase;

import dev.jossegonnza.personal_finance_manager.application.port.in.RegisterTransactionCommand;
import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTransactionServiceTest {
    static class InMemoryAccountRepository implements AccountRepository {
        private final Map<UUID, Account> storage = new HashMap<>();

        @Override
        public Optional<Account> findById(UUID accountId) {
            return Optional.ofNullable(storage.get(accountId));
        }

        @Override
        public void save(Account account) {
            storage.put(account.id(), account);
        }
    }

    static class InMemoryTransactionRepository implements TransactionRepository {
        private final List<Transaction> storage = new ArrayList<>();

        @Override
        public void save(Transaction transaction) {
            storage.add(transaction);
        }

        @Override
        public List<Transaction> findAll() {
            return Collections.unmodifiableList(storage);
        }
    }

    @Test
    void shouldRegisterIncome() {
        //Arrange
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();

        Account existingAccount = new Account(UUID.randomUUID(), "Personal", CurrencyType.EUR);
        accountRepository.save(existingAccount);

        RegisterTransactionService service = new RegisterTransactionService(
                accountRepository,
                transactionRepository
        );

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
        assertEquals(existingAccount.id(), transaction.accountId());
        assertEquals(TransactionType.INCOME, transaction.type());
        assertEquals(new Money(new BigDecimal("1000.00"), CurrencyType.EUR), transaction.amount());
        assertEquals("Salary", transaction.description());
        assertEquals(LocalDateTime.of(2025, 2, 8, 10, 25), transaction.occurredAt());
    }

    @Test
    void shouldAddTransactionWhenRegisterIncome() {
        //Arrange
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();

        Account existingAccount = new Account(UUID.randomUUID(), "Personal", CurrencyType.EUR);
        accountRepository.save(existingAccount);

        RegisterTransactionService service = new RegisterTransactionService(
                accountRepository,
                transactionRepository
        );

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
    void shouldUpdateAccountBalance() {
        //Arrange
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();

        Account existingAccount = new Account(UUID.randomUUID(), "Personal", CurrencyType.EUR);
        accountRepository.save(existingAccount);

        RegisterTransactionService service = new RegisterTransactionService(
                accountRepository,
                transactionRepository
        );

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
}
