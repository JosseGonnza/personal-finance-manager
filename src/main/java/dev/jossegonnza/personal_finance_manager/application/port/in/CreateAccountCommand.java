package dev.jossegonnza.personal_finance_manager.application.port.in;

import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;

import java.util.UUID;

public record CreateAccountCommand(
        UUID userId,
        String name,
        CurrencyType currency
) {}
