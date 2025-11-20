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
        String colorHex = "Purple";

        //Act
        Category category = new Category(
                userId,
                name,
                kind,
                colorHex
        );

        //Assert
        assertNotNull(category.id());
        assertEquals(userId, category.userId());
        assertEquals(name, category.name());
        assertEquals(CategoryKind.EXPENSE, category.kind());
        assertEquals(colorHex, category.colorHex());
    }

    @Test
    void shouldNotAllowEmptyName() {
        //Arrange
        UUID userId = UUID.randomUUID();
        CategoryKind kind = CategoryKind.EXPENSE;
        String colorHex = "Purple";

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Category(
                        userId,
                        "",
                        kind,
                        colorHex
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
        String colorHex = "Purple";

        //Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Category(
                        userId,
                        null,
                        kind,
                        colorHex
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
        String colorHex = "Purple";

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Category(
                        null,
                        name,
                        kind,
                        colorHex
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
        String colorHex = "Purple";

        //Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Category(
                        userId,
                        name,
                        null,
                        colorHex
                )
        );

        //Assert
        assertTrue(exception.getMessage().contains("null"));
    }
}
