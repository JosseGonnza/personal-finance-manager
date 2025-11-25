package dev.jossegonnza.personal_finance_manager.application.exception;

import java.util.UUID;

public class CategoryInUseException extends RuntimeException {
    private final UUID categoryId;

    public CategoryInUseException(UUID categoryId) {
        super("Category in use: " + categoryId);
        this.categoryId = categoryId;
    }

    public UUID categoryId() {
        return categoryId;
    }
}
