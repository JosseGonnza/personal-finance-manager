package dev.jossegonnza.personal_finance_manager.application.usecase;

import dev.jossegonnza.personal_finance_manager.application.port.in.CreateCategoryCommand;
import dev.jossegonnza.personal_finance_manager.application.port.out.CategoryRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreateCategoryServiceTest {
    static class InMemoryCategoryRepository implements CategoryRepository {
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

    @Test
    void shouldCreateCategory() {
        //Arrange
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        CreateCategoryService service = new CreateCategoryService(categoryRepository);
        UUID userId = UUID.randomUUID();
        CreateCategoryCommand command = new CreateCategoryCommand(
                userId,
                "Restaurantes",
                CategoryKind.EXPENSE,
                "Purple"
        );

        //Act
        Category category = service.create(command);

        //Assert
        assertNotNull(category.id());
        assertEquals(userId, category.userId());
        assertEquals("Restaurantes", category.name());
        assertEquals(CategoryKind.EXPENSE, category.kind());
        assertEquals("Purple", category.colorHex());
        assertTrue(categoryRepository.findById(category.id()).isPresent());
    }
}
