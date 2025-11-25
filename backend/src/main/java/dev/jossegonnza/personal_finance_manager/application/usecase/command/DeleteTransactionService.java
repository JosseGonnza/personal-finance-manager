package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.exception.TransactionNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.DeleteTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.AccountRepository;
import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DeleteTransactionService implements DeleteTransactionUseCase {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public DeleteTransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void deleteById(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        Account account = accountRepository.findById(transaction.accountId())
                .orElseThrow(() -> new AccountNotFoundException(transaction.accountId()));

        // Revertimos el efecto de la transacción sobre el balance
        BigDecimal currentBalance = account.balance();
        BigDecimal newBalance = currentBalance.subtract(signedAmount(transaction));

        account.updateBalance(newBalance);
        accountRepository.save(account);

        transactionRepository.deleteById(transactionId);
    }

    private BigDecimal signedAmount(Transaction tx) {
        BigDecimal value = tx.amount().value();
        return tx.type() == TransactionType.INCOME
                ? value
                : value.negate();
    }
}

