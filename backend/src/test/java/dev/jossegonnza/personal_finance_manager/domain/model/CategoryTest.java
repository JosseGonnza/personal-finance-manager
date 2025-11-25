package dev.jossegonnza.personal_finance_manager.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    @Test
    void shouldCreateValidCategory() {
        //Arrange
        UUID userId = UUID.randomUUID();
        String name = "Restaurantes";
        CategoryKind kind = CategoryKind.EXPENSE;
        CategoryColor orange = CategoryColor.ORANGE;

        //Act
        Category category = new Category(
                userId,
                name,
                kind,
                orange
        );

        //Assert
        assertNotNull(category.id());
        assertEquals(userId, category.userId());
        assertEquals(name, category.name());
        assertEquals(CategoryKind.EXPENSE, category.kind());
        assertEquals(orange, category.color());
    }

    @Test
    void shouldNotAllowEmptyName() {
        //Arrange
        UUID userId = UUID.randomUUID();
        CategoryKind kind = CategoryKind.EXPENSE;
        CategoryColor orange = CategoryColor.ORANGE;

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Category(
                        userId,
                        "",
                        kind,
                        orange
                )
        );

        //Assert
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    void shouldNotAllowNullName() {
        //Arrange
        UUID userId = UUID.randomUUID();
        CategoryKind kind = CategoryKind.EXPENSE;
        CategoryColor orange = CategoryColor.ORANGE;

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Category(
                        userId,
                        null,
                        kind,
                        orange
                )
        );

        //Assert
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    void shouldNotAcceptNullUserId() {
        //Arrange
        String name = "Restaurantes";
        CategoryKind kind = CategoryKind.EXPENSE;
        CategoryColor orange = CategoryColor.ORANGE;
        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Category(
                        null,
                        name,
                        kind,
                        orange
                )
        );

        //Assert
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    void shouldNotAcceptNullCategoryKind() {
        //Arrange
        UUID userId = UUID.randomUUID();
        String name = "Restaurantes";
        CategoryColor orange = CategoryColor.ORANGE;

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Category(
                        userId,
                        name,
                        null,
                        orange
                )
        );

        //Assert
        assertTrue(exception.getMessage().contains("null"));
    }
}
