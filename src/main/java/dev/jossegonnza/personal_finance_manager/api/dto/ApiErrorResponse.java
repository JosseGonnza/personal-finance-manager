package dev.jossegonnza.personal_finance_manager.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato estándar de error de la API")
public record ApiErrorResponse(
        @Schema(description = "Código de error de la aplicación", example = "ACCOUNT_NOT_FOUND")
        String error,
        @Schema(description = "Mensaje detallado de lo que ha ocurrido")
        String message
) {}
