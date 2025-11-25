package dev.jossegonnza.personal_finance_manager.application.port.in.query;

import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;

import java.util.List;

public interface ListCategoryColorsUseCase {
    List<CategoryColor> listAll();
}
