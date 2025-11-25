package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Respuesta con información de una cuenta.")
public record AccountResponse(
        @Schema(description = "ID de la cuenta.", example = "4fa74b62-a10b-4bb6-9ea0-0009fb682e72")
        UUID id,

        @Schema(description = "ID del usuario dueño de la cuenta.", example = "b87d2c32-8667-45a4-b8b7-61e84f34cf02")
        UUID userId,

        @Schema(description = "Nombre de la cuenta.", example = "Personal")
        String name,

        @Schema(description = "Tipo de divisa de la cuenta.", example = "EUR")
        CurrencyType currency,

        @Schema(description = "Balance actual de la cuenta.", example = "2150.50")
        BigDecimal balance
) {

    public static AccountResponse fromDomain(Account account) {
        return new AccountResponse(
                account.id(),
                account.userId(),
                account.name(),
                account.currencyType(),
                account.balance()
        );
    }
}
