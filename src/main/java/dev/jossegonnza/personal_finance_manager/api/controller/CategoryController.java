package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.*;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.*;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetUserCategoriesUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.ListCategoryColorsUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@Tag(
        name = "Categories",
        description = "Gestión de categorías para organizar ingresos y gastos."
)
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;
    private final GetUserCategoriesUseCase getUserCategoriesUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final ListCategoryColorsUseCase listCategoryColorsUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase,
                              GetCategoryUseCase getCategoryUseCase,
                              GetUserCategoriesUseCase getUserCategoriesUseCase,
                              DeleteCategoryUseCase deleteCategoryUseCase,
                              UpdateCategoryUseCase updateCategoryUseCase,
                              ListCategoryColorsUseCase listCategoryColorsUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
        this.getUserCategoriesUseCase = getUserCategoriesUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.listCategoryColorsUseCase = listCategoryColorsUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Crear una nueva categoría",
            description = "Permite registrar una categoría asociada a un usuario."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Categoría creada correctamente",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    public ResponseEntity<CategoryResponse> create(@RequestBody CreateCategoryRequest request) {
        CreateCategoryCommand command = new CreateCategoryCommand(
                request.userId(),
                request.name(),
                request.kind(),
                request.color()
        );

        Category category = createCategoryUseCase.create(command);

        return ResponseEntity
                .status(201)
                .body(CategoryResponse.fromDomain(category));
    }

    @GetMapping("/{categoryId}")
    @Operation(
            summary = "Obtener una categoría por ID",
            description = "Devuelve los datos de una categoría ya registrada."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categoría encontrada",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<CategoryResponse> getById(
            @Parameter(description = "Identificador único de la categoría")
            @PathVariable UUID categoryId) {
        Category category = getCategoryUseCase.getById(categoryId);
        return ResponseEntity
                .ok(CategoryResponse.fromDomain(category));
    }

    @GetMapping("/users/{userId}")
    @Operation(
            summary = "Listar categorías por usuario",
            description = "Devuelve todas las categorías creadas por un usuario."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Listado obtenido correctamente",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    public ResponseEntity<List<CategoryResponse>> getByUserId(
            @Parameter(description = "ID del usuario que posee las categorías")
            @PathVariable UUID userId) {
        List<Category> categories = getUserCategoriesUseCase.getByUserId(userId);
        List<CategoryResponse> response = categories.stream()
                .map(CategoryResponse :: fromDomain)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina una categoría existente por ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Categoría eliminada correctamente"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la categoría a eliminar")
            @PathVariable UUID categoryId) {
        deleteCategoryUseCase.deleteById(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{categoryId}")
    @Operation(
            summary = "Actualizar categoría",
            description = "Modifica el nombre, tipo o color de una categoría ya existente."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categoría actualizada",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Categoría no encontrada",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ResponseEntity<CategoryResponse> update(
            @Parameter(description = "ID de la categoría a actualizar")
            @PathVariable UUID categoryId,
            @RequestBody UpdateCategoryRequest request
    ) {
        UpdateCategoryCommand command = new UpdateCategoryCommand(
                request.name(),
                request.kind(),
                request.color()
        );

        Category updated = updateCategoryUseCase.update(categoryId, command);

        return ResponseEntity
                .ok(CategoryResponse.fromDomain(updated));
    }

    @Operation(
            summary = "List available category colors",
            description = "Returns the predefined colors that can be used when creating or updating categories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of available colors",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryColorResponse.class))
            )
    )
    @GetMapping("/colors")
    public ResponseEntity<List<CategoryColorResponse>> getAvailableColors() {
        List<CategoryColor> colors = listCategoryColorsUseCase.listAll();

        List<CategoryColorResponse> response = colors.stream()
                .map(CategoryColorResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }
}
