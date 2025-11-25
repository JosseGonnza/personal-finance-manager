package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccountTransactionsFilter {

    private final TransactionType type;
    private final UUID categoryId;
    private final LocalDateTime from;
    private final LocalDateTime to;

    public AccountTransactionsFilter(TransactionType type,
                                     UUID categoryId,
                                     LocalDateTime from,
                                     LocalDateTime to) {
        this.type = type;
        this.categoryId = categoryId;
        this.from = from;
        this.to = to;
    }

    public TransactionType type() {
        return type;
    }

    public UUID categoryId() {
        return categoryId;
    }

    public LocalDateTime from() {
        return from;
    }

    public LocalDateTime to() {
        return to;
    }
}
