package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.exception.CategoryNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.UpdateCategoryCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.UpdateCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryCategoryRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdateCategoryServiceTest {

    @Test
    void shouldKeepingIdAndUserIdWhenUpdateCategoryFields() {
        // Arrange
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        UpdateCategoryUseCase service = new UpdateCategoryService(categoryRepository);

        UUID userId = UUID.randomUUID();
        Category original = new Category(
                userId,
                "Shopping",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );
        categoryRepository.save(original);

        UpdateCategoryCommand command = new UpdateCategoryCommand(
                "Groceries",
                CategoryKind.EXPENSE,
                CategoryColor.GREEN
        );

        // Act
        Category updated = service.update(original.id(), command);

        // Assert
        assertEquals(original.id(), updated.id());
        assertEquals(original.userId(), updated.userId());
        assertEquals("Groceries", updated.name());
        assertEquals(CategoryKind.EXPENSE, updated.kind());
        assertEquals(CategoryColor.GREEN, updated.color());

        Category fromRepo = categoryRepository.findById(original.id()).orElseThrow();
        assertEquals(updated.name(), fromRepo.name());
        assertEquals(updated.kind(), fromRepo.kind());
        assertEquals(updated.color(), fromRepo.color());
    }

    @Test
    void shouldThrowWhenCategoryDoesNotExist() {
        // Arrange
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        UpdateCategoryUseCase service = new UpdateCategoryService(categoryRepository);

        UUID unknownId = UUID.randomUUID();
        UpdateCategoryCommand command = new UpdateCategoryCommand(
                "Whatever",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );

        // Act + Assert
        assertThrows(
                CategoryNotFoundException.class,
                () -> service.update(unknownId, command)
        );
    }
}
