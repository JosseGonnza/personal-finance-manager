package dev.jossegonnza.personal_finance_manager.application.usecase;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.RegisterTransactionCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.RegisterTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.Money;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import org.springframework.stereotype.Service;

@Service
public class RegisterTransactionService implements RegisterTransactionUseCase {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public RegisterTransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction register(RegisterTransactionCommand command) throws AccountNotFoundException {
        Account account = accountRepository.findById(command.accountId())
                .orElseThrow(() -> new AccountNotFoundException(command.accountId()));
        Money money = new Money(command.amount(), command.currency());

        Transaction transaction;
        if (command.type() == TransactionType.INCOME){
            transaction = account.registerIncome(
                    money,
                    command.categoryId(),
                    command.description(),
                    command.occurredAt());
        } else if (command.type() == TransactionType.EXPENSE) {
            transaction = account.registerExpense(
                    money,
                    command.categoryId(),
                    command.description(),
                    command.occurredAt());
        } else {
            throw new IllegalArgumentException("unsupported transaction type: " + command.type());
        }

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return transaction;
    }
}
