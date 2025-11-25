package dev.jossegonnza.personal_finance_manager.infrastructure.persistence.jpa;

import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountRepositoryJpaAdapter implements AccountRepository {

    private final AccountJpaRepository jpaRepository;

    public AccountRepositoryJpaAdapter(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Account> findById(UUID accountId) {
        return jpaRepository.findById(accountId)
                .map(this :: toDomain);
    }

    @Override
    public void save(Account account) {
        AccountEntity entity = toEntity(account);
        jpaRepository.save(entity);
    }

    //Mappers
    private Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getCurrencyType(),
                entity.getBalance()
        );
    }


    private AccountEntity toEntity(Account account) {
        return new AccountEntity(
                account.id(),
                account.userId(),
                account.name(),
                account.currencyType(),
                account.balance()
        );
    }

}
