package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.port.in.query.ListCategoryColorsUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCategoryColorsService implements ListCategoryColorsUseCase {

    @Override
    public List<CategoryColor> listAll() {
        // Devolvemos los colores definidos en el enum CategoryColor
        return List.of(CategoryColor.values());
    }
}
