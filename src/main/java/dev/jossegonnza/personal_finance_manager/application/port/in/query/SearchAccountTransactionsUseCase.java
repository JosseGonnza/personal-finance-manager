package dev.jossegonnza.personal_finance_manager.application.port.in.query;

import dev.jossegonnza.personal_finance_manager.application.usecase.query.AccountTransactionsFilter;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface SearchAccountTransactionsUseCase {
    List<Transaction> search(UUID accountId, AccountTransactionsFilter filter);
}
