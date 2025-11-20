package dev.jossegonnza.personal_finance_manager.infrastructure.persistence;

import dev.jossegonnza.personal_finance_manager.application.port.out.CategoryRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {
    private final Map<UUID, Category> storage = new HashMap<>();

    @Override
    public Optional<Category> findById(UUID categoryId) {
        return Optional.ofNullable(storage.get(categoryId));
    }

    @Override
    public void save(Category category) {
        storage.put(category.id(), category);
    }
}
