package dev.jossegonnza.personal_finance_manager.application.port.in.query;

import dev.jossegonnza.personal_finance_manager.domain.model.Category;

import java.util.List;
import java.util.UUID;

public interface GetUserCategoriesUseCase {
    List<Category> getByUserId(UUID userId);
}
