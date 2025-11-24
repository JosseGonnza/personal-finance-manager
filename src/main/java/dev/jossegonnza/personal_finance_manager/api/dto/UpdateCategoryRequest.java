package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;

public record UpdateCategoryRequest(
        String name,
        CategoryKind kind,
        CategoryColor color
) {}

