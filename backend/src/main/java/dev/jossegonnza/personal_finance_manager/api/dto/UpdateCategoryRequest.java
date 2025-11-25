package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Petición para actualizar una categoría.")
public record UpdateCategoryRequest(
        @Schema(description = "Nuevo nombre de la categoría.", example = "Groceries")
        String name,

        @Schema(description = "Nuevo tipo.", example = "EXPENSE")
        CategoryKind kind,

        @Schema(description = "Nuevo color.", example = "GREEN")
        CategoryColor color
) {}


