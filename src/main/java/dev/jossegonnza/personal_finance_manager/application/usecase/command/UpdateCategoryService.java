package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.exception.CategoryNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.UpdateCategoryCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.UpdateCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.CategoryRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateCategoryService implements UpdateCategoryUseCase {
    private final CategoryRepository categoryRepository;

    public UpdateCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category update(UUID categoryId, UpdateCategoryCommand command) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        Category updated = new Category(
                existing.id(),
                existing.userId(),
                command.name(),
                command.kind(),
                command.color()
        );

        categoryRepository.save(updated);
        return updated;
    }
}
