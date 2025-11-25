package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Respuesta con la información de una transacción.")
public record TransactionResponse(
        @Schema(description = "ID de la transacción.", example = "fa0be551-6b99-4bd9-a762-f97d1a3d8a55")
        UUID id,

        @Schema(description = "ID de la cuenta asociada.", example = "4fa74b62-a10b-4bb6-9ea0-0009fb682e72")
        UUID accountId,

        @Schema(description = "Tipo de transacción.", example = "INCOME")
        TransactionType type,

        @Schema(description = "Cantidad monetaria.", example = "100.00")
        BigDecimal amount,

        @Schema(description = "Moneda de la transacción.", example = "EUR")
        CurrencyType currency,

        @Schema(description = "ID de la categoría.", example = "c5251b95-2091-44ae-9d41-f62d1eacb2a9")
        UUID categoryId,

        @Schema(description = "Descripción de la transacción.", example = "Salary")
        String description,

        @Schema(description = "Fecha en que ocurrió.", example = "2025-02-08T10:30:00")
        LocalDateTime occurredAt
) {
    public static TransactionResponse fromDomain(Transaction transaction) {
        return new TransactionResponse(
                transaction.id(),
                transaction.accountId(),
                transaction.type(),
                transaction.amount().value(),
                transaction.amount().type(),
                transaction.categoryId(),
                transaction.description(),
                transaction.occurredAt()
        );
    }
}
