package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetUserCategoriesUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.CategoryRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetUserCategoriesService implements GetUserCategoriesUseCase {
    private final CategoryRepository categoryRepository;

    public GetUserCategoriesService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getByUserId(UUID userId) {
        return categoryRepository.findByUserId(userId);
    }
}
