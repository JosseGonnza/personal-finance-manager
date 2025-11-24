package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.exception.TransactionNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.UpdateTransactionCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.UpdateTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.Money;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UpdateTransactionService implements UpdateTransactionUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public UpdateTransactionService(AccountRepository accountRepository,
                                    TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction update(UUID transactionId, UpdateTransactionCommand command) {
        Transaction oldTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        Account account = accountRepository.findById(oldTransaction.accountId())
                .orElseThrow(() -> new AccountNotFoundException(oldTransaction.accountId()));

        BigDecimal balance = account.balance()
                .subtract(signedAmount(oldTransaction));

        Money newAmount = new Money(command.amount(), command.currency());

        Transaction updatedTransaction = new Transaction(
                oldTransaction.id(),
                oldTransaction.accountId(),
                command.type(),
                newAmount,
                command.categoryId(),
                command.description(),
                command.occurredAt()
        );

        balance = balance.add(signedAmount(updatedTransaction));

        Account updatedAccount = account.updateBalance(balance);
        accountRepository.save(updatedAccount);
        transactionRepository.save(updatedTransaction);

        return updatedTransaction;
    }

    private BigDecimal signedAmount(Transaction tx) {
        BigDecimal value = tx.amount().value();
        return tx.type() == TransactionType.INCOME
                ? value
                : value.negate(); //los gastos restan
    }
}
