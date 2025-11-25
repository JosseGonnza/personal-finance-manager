package dev.jossegonnza.personal_finance_manager.application.port.in.query;

import dev.jossegonnza.personal_finance_manager.domain.model.Account;

import java.util.UUID;

public interface GetAccountUseCase {
    Account getById(UUID accountId);
}
