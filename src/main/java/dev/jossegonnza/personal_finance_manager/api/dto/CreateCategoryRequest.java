package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;

import java.util.UUID;

public record CreateCategoryRequest(
        UUID userId,
        String name,
        CategoryKind kind,
        String colorHex
) {}
