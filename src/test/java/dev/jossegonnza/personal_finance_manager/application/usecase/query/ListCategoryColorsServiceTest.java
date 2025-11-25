package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.port.in.query.ListCategoryColorsUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCategoryColorsServiceTest {

    @Test
    void shouldReturnAllCategoryColors() {
        // Arrange
        ListCategoryColorsUseCase service = new ListCategoryColorsService();

        // Act
        List<CategoryColor> result = service.listAll();

        // Assert
        assertEquals(List.of(CategoryColor.values()), result);
    }
}
