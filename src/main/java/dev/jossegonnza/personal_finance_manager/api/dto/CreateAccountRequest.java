package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Petición para crear una nueva cuenta.")
public record CreateAccountRequest(
        @Schema(description = "ID del usuario dueño de la cuenta.", example = "b87d2c32-8667-45a4-b8b7-61e84f34cf02")
        UUID userId,

        @Schema(description = "Nombre de la cuenta.", example = "Personal")
        String name,

        @Schema(description = "Tipo de divisa de la cuenta.", example = "EUR")
        CurrencyType currency
) {}

