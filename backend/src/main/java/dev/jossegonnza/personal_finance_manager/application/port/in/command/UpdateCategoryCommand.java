package dev.jossegonnza.personal_finance_manager.application.port.in.command;

import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;

public record UpdateCategoryCommand(
        String name,
        CategoryKind kind,
        CategoryColor color
) {}
