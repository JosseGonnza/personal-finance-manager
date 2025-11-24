package dev.jossegonnza.personal_finance_manager.application.port.in.command;

import dev.jossegonnza.personal_finance_manager.domain.model.Category;

import java.util.UUID;

public interface UpdateCategoryUseCase {
    Category update(UUID categoryId, UpdateCategoryCommand command);
}
