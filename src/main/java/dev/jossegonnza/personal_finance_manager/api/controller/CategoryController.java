package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.CategoryResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateCategoryRequest;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateCategoryCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetUserCategoriesUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;
    private final GetUserCategoriesUseCase getUserCategoriesUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase,
                              GetCategoryUseCase getCategoryUseCase,
                              GetUserCategoriesUseCase getUserCategoriesUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
        this.getUserCategoriesUseCase = getUserCategoriesUseCase;
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

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<CategoryResponse>> getByUserId(@PathVariable UUID userId) {
        List<Category> categories = getUserCategoriesUseCase.getByUserId(userId);
        List<CategoryResponse> response = categories.stream()
                .map(CategoryResponse :: fromDomain)
                .toList();
        return ResponseEntity.ok(response);
    }
}
