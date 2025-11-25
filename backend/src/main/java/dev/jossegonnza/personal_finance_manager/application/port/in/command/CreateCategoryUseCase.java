package dev.jossegonnza.personal_finance_manager.application.port.in.command;

import dev.jossegonnza.personal_finance_manager.domain.model.Category;

public interface CreateCategoryUseCase {
    Category create(CreateCategoryCommand command);
}
