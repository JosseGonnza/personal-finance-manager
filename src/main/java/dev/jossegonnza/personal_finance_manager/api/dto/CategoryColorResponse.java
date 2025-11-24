package dev.jossegonnza.personal_finance_manager.api.dto;

import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;

public record CategoryColorResponse(
        String name,
        String hex
) {
    public static CategoryColorResponse fromDomain(CategoryColor color) {
        return new CategoryColorResponse(
                color.name(),
                color.hex()
        );
    }
}
