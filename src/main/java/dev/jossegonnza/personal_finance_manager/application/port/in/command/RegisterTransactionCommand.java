package dev.jossegonnza.personal_finance_manager.application.port.in.command;

import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterTransactionCommand(
        UUID accountId,
        TransactionType type,
        BigDecimal amount,
        CurrencyType currency,
        UUID categoryId,
        String description,
        LocalDateTime occurredAt
) {}
