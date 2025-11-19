package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.domain.model.Money;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        UUID userId,
        String name,
        CurrencyType currency,
        BigDecimal balance
) {
    public static AccountResponse fromDomain(Account account) {
        return new AccountResponse(
                account.id(),
                account.userId(),
                account.name(),
                account.type(),
                account.balance().amount()
        );
    }
}
