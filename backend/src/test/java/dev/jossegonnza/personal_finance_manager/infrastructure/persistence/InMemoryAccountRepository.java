package dev.jossegonnza.personal_finance_manager.infrastructure.persistence;

import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepository {
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

