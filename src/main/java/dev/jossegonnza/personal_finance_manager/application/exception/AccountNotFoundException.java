package dev.jossegonnza.personal_finance_manager.application.exception;

import java.util.UUID;

public class AccountNotFoundException extends Throwable {
    private final UUID accountId;

    public AccountNotFoundException(UUID accountId) {
        super("Account not found: " + accountId);
        this.accountId = accountId;
    }

    public UUID accountId() {
        return accountId;
    }
}
