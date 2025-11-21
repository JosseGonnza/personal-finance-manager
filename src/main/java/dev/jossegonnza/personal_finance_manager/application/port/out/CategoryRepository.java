package dev.jossegonnza.personal_finance_manager.application.port.out;

import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Optional<Category> findById(UUID categoryId);
    void save(Category category);
}
