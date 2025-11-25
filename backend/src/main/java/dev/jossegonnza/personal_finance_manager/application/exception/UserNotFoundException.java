package dev.jossegonnza.personal_finance_manager.application.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    private final UUID userId;

    public UserNotFoundException(UUID userId) {
        super("User not found: " + userId);
        this.userId = userId;
    }

    public UUID userId() {
        return userId;
    }
}
