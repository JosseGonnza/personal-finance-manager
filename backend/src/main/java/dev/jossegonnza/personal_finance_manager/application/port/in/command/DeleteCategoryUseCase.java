package dev.jossegonnza.personal_finance_manager.application.port.in.command;

import java.util.UUID;

public interface DeleteCategoryUseCase {
    void deleteById(UUID categoryId);
}
