package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountTransactionsUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetAccountTransactionsService implements GetAccountTransactionsUseCase {
    private  final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public GetAccountTransactionsService(AccountRepository accountRepository,
                                         TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> getByAccountId(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        return transactionRepository.findAllByAccountId(accountId);
    }
}
