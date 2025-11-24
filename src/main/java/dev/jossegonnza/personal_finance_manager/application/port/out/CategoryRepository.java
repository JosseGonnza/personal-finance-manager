package dev.jossegonnza.personal_finance_manager.application.port.out;

import dev.jossegonnza.personal_finance_manager.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Optional<Category> findById(UUID categoryId);
    void save(Category category);
    List<Category> findAll();
    List<Category> findByUserId(UUID userId);
    void deleteById(UUID categoryId);
}
