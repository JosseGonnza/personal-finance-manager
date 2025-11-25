package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.exception.CategoryNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryCategoryRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetCategoryServiceTest {

    @Test
    void shouldReturnCategoryWhenExist() {
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        GetCategoryUseCase service = new GetCategoryService(categoryRepository);

        Category existing = new Category(
                UUID.randomUUID(),
                "Restaurantes",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );
        categoryRepository.save(existing);

        //Act
        Category category = service.getById(existing.id());

        //Assert
        assertEquals(existing.id(), category.id());
        assertEquals(existing.userId(), category.userId());
        assertEquals(existing.name(), category.name());
        assertEquals(existing.kind(), category.kind());
        assertEquals(existing.color(), category.color());
    }

    @Test
    void shouldThrowWhenCategoryDoesNotExist() {
        //Arrange
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        GetCategoryUseCase service = new GetCategoryService(categoryRepository);
        UUID unknowId = UUID.randomUUID();

        //Act
        Exception exception = assertThrows(
                CategoryNotFoundException.class,
                () -> service.getById(unknowId)

        );

        //Assert
        assertEquals("Category not found: " + unknowId, exception.getMessage());
    }
}
