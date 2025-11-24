package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateTransactionRequest(
        TransactionType transactionType,
        BigDecimal amount,
        CurrencyType currencyType,
        UUID categoryId,
        String description,
        LocalDateTime occurredAt) {}
