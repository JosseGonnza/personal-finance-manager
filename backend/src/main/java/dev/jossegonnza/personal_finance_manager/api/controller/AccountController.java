package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.AccountResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.ApiErrorResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateAccountRequest;
import dev.jossegonnza.personal_finance_manager.api.dto.TransactionResponse;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountTransactionsUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.SearchAccountTransactionsUseCase;
import dev.jossegonnza.personal_finance_manager.application.usecase.query.AccountTransactionsFilter;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@Tag(
        name = "Accounts",
        description = "Gestión de cuentas y consulta de movimientos."
)
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final GetAccountTransactionsUseCase getAccountTransactionsUseCase;
    private SearchAccountTransactionsUseCase searchAccountTransactionsUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase,
                             GetAccountUseCase getAccountUseCase,
                             GetAccountTransactionsUseCase getAccountTransactionsUseCase,
                             SearchAccountTransactionsUseCase searchAccountTransactionsUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountUseCase = getAccountUseCase;
        this.getAccountTransactionsUseCase = getAccountTransactionsUseCase;
        this.searchAccountTransactionsUseCase = searchAccountTransactionsUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Crear una nueva cuenta",
            description = "Crea una cuenta asociada a un usuario con saldo inicial 0."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Cuenta creada correctamente",
            content = @Content(schema = @Schema(implementation = AccountResponse.class))
    )
    public ResponseEntity<AccountResponse> create(@RequestBody CreateAccountRequest request) {
        CreateAccountCommand command = new CreateAccountCommand(
                request.userId(),
                request.name(),
                request.currency()
        );

        Account account = createAccountUseCase.create(command);

        return ResponseEntity
                .status(201)
                .body(AccountResponse.fromDomain(account));
    }

    @GetMapping("/{accountId}")
    @Operation(
            summary = "Obtener una cuenta por ID",
            description = "Devuelve los datos de una cuenta existente, incluido el saldo actual."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Cuenta encontrada",
            content = @Content(schema = @Schema(implementation = AccountResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Cuenta no encontrada",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<AccountResponse> getById(
            @Parameter(description = "ID de la cuenta")
            @PathVariable UUID accountId) {
        Account account = getAccountUseCase.getById(accountId);
        return ResponseEntity
                .ok(AccountResponse.fromDomain(account));
    }

    @GetMapping("/{accountId}/transactions")
    @Operation(
            summary = "Listar transacciones de una cuenta",
            description = "Devuelve todas las transacciones registradas para una cuenta."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Listado de transacciones obtenido",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Cuenta no encontrada",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @Parameter(description = "ID de la cuenta")
            @PathVariable UUID accountId) {
        List<Transaction> transactions = getAccountTransactionsUseCase.getByAccountId(accountId);

        List<TransactionResponse> response = transactions.stream()
                .map(TransactionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}/transactions/search")
    @Operation(
            summary = "Buscar transacciones con filtros",
            description = """
                    Permite filtrar las transacciones de una cuenta por:
                    - tipo (ingreso/gasto)
                    - categoría
                    - rango de fechas [from, to]
                    Todos los filtros son opcionales y combinables.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Listado filtrado obtenido",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Cuenta no encontrada",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<List<TransactionResponse>> searchTransactions(
            @Parameter(description = "ID de la cuenta sobre la que buscar")
            @PathVariable UUID accountId,

            @Parameter(description = "Tipo de transacción: INCOME o EXPENSE")
            @RequestParam(required = false) TransactionType type,

            @Parameter(description = "ID de la categoría por la que filtrar")
            @RequestParam(required = false) UUID categoryId,

            @Parameter(description = "Fecha/hora mínima (incluida) en formato ISO-8601")
            @RequestParam(required = false) LocalDateTime from,

            @Parameter(description = "Fecha/hora máxima (incluida) en formato ISO-8601")
            @RequestParam(required = false) LocalDateTime to
            ) {
        AccountTransactionsFilter filter = new AccountTransactionsFilter(
                type,
                categoryId,
                from,
                to
        );

        List<Transaction> result = searchAccountTransactionsUseCase.search(accountId, filter);
        List<TransactionResponse> response = result.stream()
                .map(TransactionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }
}
