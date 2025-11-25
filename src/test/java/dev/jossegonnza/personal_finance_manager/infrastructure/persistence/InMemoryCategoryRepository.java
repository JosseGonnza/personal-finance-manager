package dev.jossegonnza.personal_finance_manager.infrastructure.persistence;

import dev.jossegonnza.personal_finance_manager.application.port.out.CategoryRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;

import java.util.*;

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

    @Override
    public List<Category> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public List<Category> findByUserId(UUID userId) {
        return storage.values()
                .stream()
                .filter(category -> category.userId().equals(userId))
                .toList();
    }

    @Override
    public void deleteById(UUID categoryId) {
        storage.remove(categoryId);
    }
}
