package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;

import java.util.UUID;

public record CreateAccountRequest(
        UUID userId,
        String name,
        CurrencyType currency
) {}
