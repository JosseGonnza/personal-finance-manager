package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetUserCategoriesUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryCategoryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetUserCategoriesServiceTest {
    @Test
    void shouldReturnCategoriesForUserWhenUserHasCategories() {
        //Arrange
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        GetUserCategoriesUseCase service = new GetUserCategoriesService(categoryRepository);

        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        Category category1 = new Category(
                userId,
                "Shopping",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );
        Category category2 = new Category(
                userId,
                "Salary",
                CategoryKind.INCOME,
                CategoryColor.RED
        );
        Category category3 = new Category(
                otherUserId,
                "Pets",
                CategoryKind.EXPENSE,
                CategoryColor.BLUE
        );
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        //Act
        List<Category> result = service.getByUserId(userId);

        //Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(category1));
        assertTrue(result.contains(category2));
        assertFalse(result.contains(category3));
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoCategories() {
        // Arrange
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        GetUserCategoriesUseCase service = new GetUserCategoriesService(categoryRepository);

        UUID userId = UUID.randomUUID();

        // Act
        List<Category> result = service.getByUserId(userId);

        // Assert
        assertTrue(result.isEmpty());
    }
}
