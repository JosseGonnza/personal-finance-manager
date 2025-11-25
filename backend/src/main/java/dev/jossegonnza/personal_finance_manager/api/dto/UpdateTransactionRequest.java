package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Petición para actualizar una transacción existente.")
public record UpdateTransactionRequest(
        @Schema(description = "Nuevo tipo de transacción.", example = "EXPENSE")
        TransactionType transactionType,

        @Schema(description = "Nueva cantidad.", example = "40.00")
        BigDecimal amount,

        @Schema(description = "Nueva divisa.", example = "EUR")
        CurrencyType currencyType,

        @Schema(description = "Nueva categoría.", example = "a1e8df2b-3cd8-4c72-8e9e-5c6aff1a7bca")
        UUID categoryId,

        @Schema(description = "Nueva descripción.", example = "Updated grocery expense")
        String description,

        @Schema(description = "Nueva fecha de la transacción.", example = "2025-03-10T12:00:00")
        LocalDateTime occurredAt
) {}

