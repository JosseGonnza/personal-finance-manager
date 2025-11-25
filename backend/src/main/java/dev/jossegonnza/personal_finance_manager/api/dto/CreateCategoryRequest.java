package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Petición para crear una categoría.")
public record CreateCategoryRequest(
        @Schema(description = "ID del usuario dueño de la categoría.", example = "b87d2c32-8667-45a4-b8b7-61e84f34cf02")
        UUID userId,

        @Schema(description = "Nombre de la categoría.", example = "Shopping")
        String name,

        @Schema(description = "Tipo de categoría.", example = "EXPENSE")
        CategoryKind kind,

        @Schema(description = "Color asignado a la categoría.", example = "PURPLE")
        CategoryColor color
) {}

