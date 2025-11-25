package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Respuesta con información de una categoría.")
public record CategoryResponse(
        @Schema(description = "ID de la categoría.", example = "c5251b95-2091-44ae-9d41-f62d1eacb2a9")
        UUID id,

        @Schema(description = "ID del usuario propietario.", example = "b87d2c32-8667-45a4-b8b7-61e84f34cf02")
        UUID userId,

        @Schema(description = "Nombre de la categoría.", example = "Pets")
        String name,

        @Schema(description = "Tipo de categoría.", example = "EXPENSE")
        CategoryKind kind,

        @Schema(description = "Color asignado.", example = "GREEN")
        CategoryColorResponse color
) {
    public static CategoryResponse fromDomain(Category category) {
        return new CategoryResponse(
                category.id(),
                category.userId(),
                category.name(),
                category.kind(),
                CategoryColorResponse.fromDomain(category.color())
        );
    }
}
