package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateCategoryCommand;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryCategoryRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreateCategoryServiceTest {

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
                CategoryColor.ORANGE
        );

        //Act
        Category category = service.create(command);

        //Assert
        assertNotNull(category.id());
        assertEquals(userId, category.userId());
        assertEquals("Restaurantes", category.name());
        assertEquals(CategoryKind.EXPENSE, category.kind());
        assertEquals(CategoryColor.ORANGE, category.color());
        assertTrue(categoryRepository.findById(category.id()).isPresent());
    }
}
