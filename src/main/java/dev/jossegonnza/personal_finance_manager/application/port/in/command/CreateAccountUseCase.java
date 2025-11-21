package dev.jossegonnza.personal_finance_manager.application.port.in.command;

import dev.jossegonnza.personal_finance_manager.domain.model.Account;

public interface CreateAccountUseCase {
    Account create(CreateAccountCommand command);
}
