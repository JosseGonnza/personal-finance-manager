package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryAccountRepository;

import java.util.UUID;

public class GetAccountService implements GetAccountUseCase {
    private final AccountRepository accountRepository;

    public GetAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
