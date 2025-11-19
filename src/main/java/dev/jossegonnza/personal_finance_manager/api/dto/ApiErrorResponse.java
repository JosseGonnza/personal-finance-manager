package dev.jossegonnza.personal_finance_manager.api.dto;

public record ApiErrorResponse(
        String error,
        String message
) {}
