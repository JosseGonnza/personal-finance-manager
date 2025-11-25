package dev.jossegonnza.personal_finance_manager.application.port.in.query;

import dev.jossegonnza.personal_finance_manager.domain.model.Category;

import java.util.UUID;

public interface GetCategoryUseCase {
    Category getById(UUID categoryId);
}
