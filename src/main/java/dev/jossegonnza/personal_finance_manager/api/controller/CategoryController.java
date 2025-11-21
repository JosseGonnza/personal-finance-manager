package dev.jossegonnza.personal_finance_manager.api.controller;

import dev.jossegonnza.personal_finance_manager.api.dto.CategoryResponse;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateCategoryRequest;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateCategoryCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
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
}
