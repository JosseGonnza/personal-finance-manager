package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        UUID userId,
        String name,
        CategoryKind kind,
        String colorHex
) {
    public static CategoryResponse fromDomain(Category category) {
        return new CategoryResponse(
                category.id(),
                category.userId(),
                category.name(),
                category.kind(),
                category.colorHex()
        );
    }
}
