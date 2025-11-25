package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.ApiErrorResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.RegisterTransactionRequest;
import dev.jossegonnza.personal_finance_manager.api.dto.TransactionResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.UpdateTransactionRequest;
import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.*;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@Tag(
        name = "Transactions",
        description = "Operaciones para registrar, consultar, actualizar y eliminar transacciones."
)
public class TransactionController {

    private final RegisterTransactionUseCase registerTransactionUseCase;
    private final GetTransactionUseCase getTransactionUseCase;
    private final UpdateTransactionUseCase updateTransactionUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;

    public TransactionController(
            RegisterTransactionUseCase registerTransactionUseCase,
            GetTransactionUseCase getTransactionUseCase,
            UpdateTransactionUseCase updateTransactionUseCase,
            DeleteTransactionUseCase deleteTransactionUseCase) {
        this.registerTransactionUseCase = registerTransactionUseCase;
        this.getTransactionUseCase = getTransactionUseCase;
        this.updateTransactionUseCase = updateTransactionUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Registrar una nueva transacción",
            description = "Crea una transacción de tipo INCOME o EXPENSE asociada a una cuenta existente."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Transacción creada correctamente",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "La cuenta asociada no existe",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<TransactionResponse> create(@RequestBody RegisterTransactionRequest request) throws AccountNotFoundException {
        RegisterTransactionCommand command = new RegisterTransactionCommand(
                request.accountId(),
                request.type(),
                request.amount(),
                request.currency(),
                request.categoryId(),
                request.description(),
                request.occurredAt()
        );

        Transaction transaction = registerTransactionUseCase.register(command);

        return ResponseEntity
                .status(201)
                .body(TransactionResponse.fromDomain(transaction));
    }

    @GetMapping("/{transactionId}")
    @Operation(
            summary = "Obtener una transacción por ID",
            description = "Devuelve los detalles completos de una transacción existente."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Transacción encontrada",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Transacción no encontrada",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<TransactionResponse> getById(
            @Parameter(description = "Identificador de la transacción")
            @PathVariable UUID transactionId) {
        Transaction transaction = getTransactionUseCase.getById(transactionId);
        return ResponseEntity
                .ok(TransactionResponse.fromDomain(transaction));
    }

    @PutMapping("/{transactionId}")
    @Operation(
            summary = "Actualizar una transacción",
            description = """
                    Actualiza los datos de una transacción existente y recalcula el balance de la cuenta \
                    aplicando la diferencia entre la transacción antigua y la nueva.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Transacción actualizada correctamente",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Transacción no encontrada",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<TransactionResponse> update(
            @Parameter(description = "Identificador de la transacción a actualizar")
            @PathVariable UUID transactionId,
            @RequestBody UpdateTransactionRequest request
            ) {
        UpdateTransactionCommand command = new UpdateTransactionCommand(
                request.transactionType(),
                request.amount(),
                request.currencyType(),
                request.categoryId(),
                request.description(),
                request.occurredAt()
        );

        Transaction updated = updateTransactionUseCase.update(transactionId, command);

        return ResponseEntity
                .ok(TransactionResponse.fromDomain(updated));
    }

    @DeleteMapping("/{transactionId}")
    @Operation(
            summary = "Eliminar una transacción",
            description = "Elimina una transacción y revierte su impacto sobre el balance de la cuenta."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Transacción eliminada correctamente (sin contenido)"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Transacción no encontrada",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador de la transacción a eliminar")
            @PathVariable UUID transactionId) {
        deleteTransactionUseCase.deleteById(transactionId);
        return ResponseEntity.noContent().build();
    }
}
