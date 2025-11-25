package dev.jossegonnza.personal_finance_manager.application.usecase.query;

import dev.jossegonnza.personal_finance_manager.application.exception.CategoryNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.exception.TransactionNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.*;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryCategoryRepository;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryTransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetTransactionServiceTest {

    @Test
    void shouldReturnTransactionWhenExist() {
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        GetTransactionUseCase service = new GetTransactionService(transactionRepository);

        UUID accountId = UUID.randomUUID();
        Transaction existing = new Transaction(
                accountId,
                TransactionType.INCOME,
                new Money(new BigDecimal("1000.00"), CurrencyType.EUR),
                UUID.randomUUID(),
                "Salary",
                LocalDateTime.of(2025, 2, 8, 10, 30)
        );
        transactionRepository.save(existing);

        //Act
        Transaction transaction = service.getById(existing.id());

        //Assert
        assertEquals(existing.id(), transaction.id());
        assertEquals(existing.accountId(), transaction.accountId());
        assertEquals(existing.type(), transaction.type());
        assertEquals(existing.amount(), transaction.amount());
        assertEquals(existing.categoryId(), transaction.categoryId());
        assertEquals(existing.description(), transaction.description());
        assertEquals(existing.occurredAt(), transaction.occurredAt());
    }

    @Test
    void shouldThrowWhenTransactionDoesNotExist() {
        //Arrange
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        GetTransactionUseCase service = new GetTransactionService(transactionRepository);
        UUID unknowId = UUID.randomUUID();

        //Act
        Exception exception = assertThrows(
                TransactionNotFoundException.class,
                () -> service.getById(unknowId)

        );

        //Assert
        assertEquals("Transaction not found: " + unknowId, exception.getMessage());
    }
}
