package dev.jossegonnza.personal_finance_manager.application.exception;

import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {
    private final UUID categoryId;

    public CategoryNotFoundException(UUID categoryId) {
        super("Category not found: " + categoryId);
        this.categoryId = categoryId;
    }

    public UUID categoryId() {
        return categoryId;
    }
}
