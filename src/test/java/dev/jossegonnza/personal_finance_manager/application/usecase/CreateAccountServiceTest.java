package dev.jossegonnza.personal_finance_manager.application.usecase;

import dev.jossegonnza.personal_finance_manager.application.port.in.CreateAccountCommand;
import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.domain.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountServiceTest {
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

    @Test
    void shouldCreateAccountWithInitialZeroBalance() {
        //Arrange
        UUID userId = UUID.randomUUID();
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        CreateAccountService service = new CreateAccountService(accountRepository);
        CreateAccountCommand command = new CreateAccountCommand(
                userId,
                "Personal",
                CurrencyType.EUR
        );

        //Act
        Account account = service.create(command);

        //Assert
        assertNotNull(account.id());
        assertEquals(userId, account.userId());
        assertEquals("Personal", account.name());
        assertEquals(CurrencyType.EUR, account.type());
        assertEquals(new Money(new BigDecimal("0.00"), CurrencyType.EUR), account.balance());
        assertTrue(accountRepository.findById(account.id()).isPresent());
    }
}
