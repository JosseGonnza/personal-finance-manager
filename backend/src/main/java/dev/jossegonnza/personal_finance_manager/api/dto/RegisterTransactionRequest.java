package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Petición para registrar una transacción.")
public record RegisterTransactionRequest(
        @Schema(description = "ID de la cuenta asociada.", example = "4fa74b62-a10b-4bb6-9ea0-0009fb682e72")
        UUID accountId,

        @Schema(description = "Tipo de transacción.", example = "INCOME")
        TransactionType type,

        @Schema(description = "Cantidad monetaria de la transacción.", example = "100.00")
        BigDecimal amount,

        @Schema(description = "Divisa de la transacción.", example = "EUR")
        CurrencyType currency,

        @Schema(description = "ID de la categoría.", example = "e7cdf673-95fa-4dc8-a133-1b645ced8153")
        UUID categoryId,

        @Schema(description = "Descripción de la transacción.", example = "Salary")
        String description,

        @Schema(description = "Fecha en que ocurrió la transacción.", example = "2025-03-01T10:30:00")
        LocalDateTime occurredAt
) {}

