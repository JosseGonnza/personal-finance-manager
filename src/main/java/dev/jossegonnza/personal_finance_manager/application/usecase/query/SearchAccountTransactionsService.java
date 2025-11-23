package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.SearchAccountTransactionsUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SearchAccountTransactionsService implements SearchAccountTransactionsUseCase {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public SearchAccountTransactionsService(AccountRepository accountRepository,
                                            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> search(UUID accountId, AccountTransactionsFilter filter) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        return transactionRepository.findAllByAccountId(accountId)
                .stream()
                .filter(t -> filter.type() == null || t.type() == filter.type())
                .toList();
    }
}
