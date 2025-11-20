package dev.jossegonnza.personal_finance_manager.application.usecase;

import dev.jossegonnza.personal_finance_manager.application.port.in.CreateCategoryCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.CreateCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.CategoryRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;

public class CreateCategoryService implements CreateCategoryUseCase {
    private final CategoryRepository categoryRepository;

    public CreateCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(CreateCategoryCommand command){
        Category category = new Category(
                command.userId(),
                command.name(),
                command.kind(),
                command.colorHex()
        );

        categoryRepository.save(category);

        return category;
    }
}
