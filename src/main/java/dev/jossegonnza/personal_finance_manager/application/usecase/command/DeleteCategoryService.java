package dev.jossegonnza.personal_finance_manager.application.usecase.command;

import dev.jossegonnza.personal_finance_manager.application.exception.CategoryInUseException;
import dev.jossegonnza.personal_finance_manager.application.exception.CategoryNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.DeleteCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.out.CategoryRepository;
import dev.jossegonnza.personal_finance_manager.application.port.out.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCategoryService implements DeleteCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public DeleteCategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public void deleteById(UUID categoryId) {
        boolean exists = categoryRepository.findById(categoryId).isPresent();
        if (!exists) {
            throw new CategoryNotFoundException(categoryId);
        }

        if (transactionRepository.existsByCategoryId(categoryId)) {
            throw new CategoryInUseException(categoryId);
        }

        categoryRepository.deleteById(categoryId);
    }
}
