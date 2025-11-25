package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryAccountRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetAccountServiceTest {

    @Test
    void shouldReturnAccountWhenExist() {
        //Arrange
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        GetAccountUseCase service = new GetAccountService(accountRepository);

        Account existing = new Account(
                UUID.randomUUID(),
                "Personal",
                CurrencyType.EUR
        );
        accountRepository.save(existing);

        //Act
        Account account = service.getById(existing.id());

        //Assert
        assertEquals(existing.id(), account.id());
        assertEquals(existing.userId(), account.userId());
        assertEquals(existing.name(), account.name());
        assertEquals(existing.currencyType(), account.currencyType());
        assertEquals(new BigDecimal("0"), account.balance());
    }

    @Test
    void shouldThrowWhenAccountDoesNotExist() {
        //Arrange
        InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
        GetAccountUseCase service = new GetAccountService(accountRepository);
        UUID unknowId = UUID.randomUUID();

        //Act
        Exception exception = assertThrows(
                AccountNotFoundException.class,
                () -> service.getById(unknowId)

        );

        //Assert
        assertEquals("Account not found: " + unknowId, exception.getMessage());
    }
}
