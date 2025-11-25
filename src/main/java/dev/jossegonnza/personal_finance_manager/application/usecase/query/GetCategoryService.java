package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.exception.CategoryNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.CategoryRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetCategoryService implements GetCategoryUseCase {
    private final CategoryRepository categoryRepository;

    public GetCategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}
