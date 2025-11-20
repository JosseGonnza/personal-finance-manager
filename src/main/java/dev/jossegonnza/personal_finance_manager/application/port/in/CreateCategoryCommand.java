package dev.jossegonnza.personal_finance_manager.application.port.in;

import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;

import java.util.UUID;

public record CreateCategoryCommand(
        UUID userId,
        String name,
        CategoryKind kind,
        String colorHex
) {
}
