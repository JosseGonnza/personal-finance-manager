package dev.jossegonnza.personal_finance_manager.application.usecase;

import dev.jossegonnza.personal_finance_manager.application.port.in.CreateAccountCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.CreateAccountUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;

public class CreateAccountService implements CreateAccountUseCase {
    private final AccountRepository accountRepository;

    public CreateAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account create(CreateAccountCommand command) {
        //TODO: No deberia de aqui crear un user y buscarlo por Id para testear que la cuenta le pertenece?
        Account account = new Account(
                command.userId(),
                command.name(),
                command.currency()
        );

        accountRepository.save(account);

        return account;
    }
}
