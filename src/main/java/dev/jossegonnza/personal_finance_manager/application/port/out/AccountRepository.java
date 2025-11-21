package dev.jossegonnza.personal_finance_manager.application.port.out;

import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findById(UUID accountId);
    void save(Account account);
}
