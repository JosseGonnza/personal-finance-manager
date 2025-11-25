package dev.jossegonnza.personal_finance_manager.infrastructure.persistence;

import dev.jossegonnza.personal_finance_manager.application.port.out.CategoryRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.jpa.CategoryEntity;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.jpa.CategoryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CategoryRepositoryJpaAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;

    public CategoryRepositoryJpaAdapter(CategoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Category> findById(UUID categoryId) {
        return jpaRepository.findById(categoryId)
                .map(this::toDomain);
    }

    @Override
    public void save(Category category) {
        CategoryEntity entity = toEntity(category);
        jpaRepository.save(entity);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Category> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID categoryId) {
        jpaRepository.deleteById(categoryId);
    }

    //Mappers
    private CategoryEntity toEntity(Category category) {
        return new CategoryEntity(
                category.id(),
                category.userId(),
                category.name(),
                category.kind(),
                category.color()
        );
    }

    private Category toDomain(CategoryEntity entity) {
        return new Category(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getKind(),
                entity.getColor()
        );
    }
}
