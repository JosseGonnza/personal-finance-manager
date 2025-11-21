package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.CategoryResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateCategoryRequest;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateCategoryCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryUseCase getCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CreateCategoryRequest request) {
        CreateCategoryCommand command = new CreateCategoryCommand(
                request.userId(),
                request.name(),
                request.kind(),
                request.colorHex()
        );

        Category category = createCategoryUseCase.create(command);

        return ResponseEntity
                .status(201)
                .body(CategoryResponse.fromDomain(category));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable UUID categoryId) {
        Category category = getCategoryUseCase.getById(categoryId);
        return ResponseEntity
                .ok(CategoryResponse.fromDomain(category));
    }
}
