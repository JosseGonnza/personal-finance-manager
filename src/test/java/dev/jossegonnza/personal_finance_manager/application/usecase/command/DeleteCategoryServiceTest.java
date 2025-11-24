package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.exception.CategoryInUseException;
import dev.jossegonnza.personal_finance_manager.application.exception.CategoryNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.DeleteCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.*;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryCategoryRepository;
import dev.jossegonnza.personal_finance_manager.infrastructure.persistence.InMemoryTransactionRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteCategoryServiceTest {

    @Test
    void shouldDeleteCategoryWhenNotUsedByTransactions() {
        //Arrange
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        DeleteCategoryUseCase service = new DeleteCategoryService(categoryRepository, transactionRepository);

        UUID userId = UUID.randomUUID();
        Category category = new Category(
                userId,
                "Shopping",
                CategoryKind.EXPENSE,
                "Red"
        );
        categoryRepository.save(category);

        //Act
        service.deleteById(category.id());

        //Assert
        assertTrue(categoryRepository.findById(category.id()).isEmpty());
    }

    @Test
    void shouldThrowWhenCategoryIsUsedByTransactions() {
        // Arrange
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        DeleteCategoryUseCase service = new DeleteCategoryService(categoryRepository, transactionRepository);

        UUID userId = UUID.randomUUID();
        Category category = new Category(
                userId,
                "Shopping",
                CategoryKind.EXPENSE,
                "Red"
        );
        categoryRepository.save(category);

        Transaction t = new Transaction(
                UUID.randomUUID(),
                TransactionType.EXPENSE,
                new Money(new BigDecimal("20.00"), CurrencyType.EUR),
                category.id(),
                "Amazon",
                java.time.LocalDateTime.now()
        );
        transactionRepository.save(t);

        // Act
        CategoryInUseException exception = assertThrows(
                CategoryInUseException.class,
                () -> service.deleteById(t.categoryId())
        );

        // Assert extra: la categoría sigue existiendo
        assertTrue(categoryRepository.findById(category.id()).isPresent());
        assertEquals(category.id(), exception.categoryId());
    }

    @Test
    void shouldThrowWhenCategoryDoesNotExist() {
        // Arrange
        InMemoryCategoryRepository categoryRepository = new InMemoryCategoryRepository();
        InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
        DeleteCategoryUseCase service = new DeleteCategoryService(categoryRepository, transactionRepository);

        UUID unknownId = UUID.randomUUID();

        // Act
        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> service.deleteById(unknownId)
        );

        // Assert
        assertEquals("Category not found: " + unknownId, exception.getMessage());
    }
}
