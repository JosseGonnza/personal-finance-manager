package dev.jossegonnza.personal_finance_manager.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;

@Schema(description = "Representa un color disponible para asignar a una categoría.")
public record CategoryColorResponse(

        @Schema(description = "Nombre del color dentro del sistema.", example = "PURPLE")
        String name,

        @Schema(description = "Código hexadecimal del color.", example = "#A020F0")
        String hex
) {
    public static CategoryColorResponse fromDomain(CategoryColor color) {
        return new CategoryColorResponse(
                color.name(),
                color.hex()
        );
    }
}

