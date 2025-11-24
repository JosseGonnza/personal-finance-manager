package dev.jossegonnza.personal_finance_manager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateCategoryRequest;
import dev.jossegonnza.personal_finance_manager.api.dto.UpdateCategoryRequest;
import dev.jossegonnza.personal_finance_manager.application.exception.CategoryInUseException;
import dev.jossegonnza.personal_finance_manager.application.exception.CategoryNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.exception.UserNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.*;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetUserCategoriesUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryColor;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private GetCategoryUseCase getCategoryUseCase;

    @MockitoBean
    private GetUserCategoriesUseCase getUserCategoriesUseCase;

    @MockitoBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockitoBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Test
    void shouldReturn201WhenCreateCategory() throws Exception {
        //Arrange
        UUID userId = UUID.randomUUID();
        CreateCategoryRequest request = new CreateCategoryRequest(
                userId,
                "Restaurantes",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );

        Category category = new Category(
                userId,
                "Restaurantes",
                CategoryKind.EXPENSE,
                CategoryColor.RED
        );

        when(createCategoryUseCase.create(any(CreateCategoryCommand.class)))
                .thenReturn(category);

        //Act + Assert
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(category.id().toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Restaurantes"))
                .andExpect(jsonPath("$.kind").value("EXPENSE"))
                .andExpect(jsonPath("$.color.name").value("RED"))
                .andExpect(jsonPath("$.color.hex").value("#EF4444"));    }

    @Test
    void shouldReturn404WhenUserNotFound() throws UserNotFoundException, Exception {
        //Arrange
        UUID userId = UUID.randomUUID();

        CreateCategoryRequest request = new CreateCategoryRequest(
                userId,
                "Restaurantes",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );

        when(createCategoryUseCase.create(any(CreateCategoryCommand.class)))
                .thenThrow(new UserNotFoundException(userId));

        //Act + Assert
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("USER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn200WhenCategoryExist() throws Exception {
        //Arrange
        UUID userId = UUID.randomUUID();
        Category category = new Category(
                userId,
                "Restaurantes",
                CategoryKind.EXPENSE,
                CategoryColor.RED
        );

        when(getCategoryUseCase.getById(category.id()))
                .thenReturn(category);

        //Act + Assert
        mockMvc.perform(get("/api/categories/{id}", category.id()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(category.id().toString()))
                .andExpect(jsonPath("$.userId").value(category.userId().toString()))
                .andExpect(jsonPath("$.name").value("Restaurantes"))
                .andExpect(jsonPath("$.kind").value("EXPENSE"))
                .andExpect(jsonPath("$.color.name").value("RED"))
                .andExpect(jsonPath("$.color.hex").value("#EF4444"));    }

    @Test
    void shouldReturn404WhenCategoryNotFound() throws Exception {
        // Arrange
        UUID unknownId = UUID.randomUUID();

        when(getCategoryUseCase.getById(unknownId))
                .thenThrow(new CategoryNotFoundException(unknownId));

        // Act + Assert
        mockMvc.perform(get("/api/categories/{id}", unknownId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("CATEGORY_NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnCategoriesWhenUserHasCategories() throws Exception {
        //Arrange
        UUID userId = UUID.randomUUID();
        Category category1 = new Category(
                userId,
                "Shopping",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );
        Category category2 = new Category(
                userId,
                "Pets",
                CategoryKind.EXPENSE,
                CategoryColor.RED
        );

        when(getUserCategoriesUseCase.getByUserId(userId))
                .thenReturn(List.of(category1, category2));

        //Act + Assert
        mockMvc.perform(get("/api/categories/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(category1.id().toString()))
                .andExpect(jsonPath("$[0].userId").value(category1.userId().toString()))
                .andExpect(jsonPath("$[0].name").value("Shopping"))
                .andExpect(jsonPath("$[0].kind").value("EXPENSE"))
                .andExpect(jsonPath("$[0].color.name").value("ORANGE"))
                .andExpect(jsonPath("$[0].color.hex").value("#F97316"))

                .andExpect(jsonPath("$[1].id").value(category2.id().toString()))
                .andExpect(jsonPath("$[1].userId").value(category2.userId().toString()))
                .andExpect(jsonPath("$[1].name").value("Pets"))
                .andExpect(jsonPath("$[1].kind").value("EXPENSE"))
                .andExpect(jsonPath("$[1].color.name").value("RED"))
                .andExpect(jsonPath("$[1].color.hex").value("#EF4444"));
    }

    @Test
    void shouldReturnEmptyArrayWhenUserHasNoCategories() throws Exception {
        //Arrange
        UUID userId = UUID.randomUUID();

        when(getUserCategoriesUseCase.getByUserId(userId))
                .thenReturn(List.of());

        //Act + Assert
        mockMvc.perform(get("/api/categories/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldReturn204WhenCategoryIsDelete() throws Exception {
        //Arrange
        UUID categoryId = UUID.randomUUID();

        mockMvc.perform(delete("/api/categories/{id}", categoryId))
                .andExpect(status().isNoContent());

        verify(deleteCategoryUseCase).deleteById(categoryId);
    }

    @Test
    void shouldReturn404WhenDeletingUnknownCategory() throws Exception {
        UUID unknownId = UUID.randomUUID();

        doThrow(new CategoryNotFoundException(unknownId))
                .when(deleteCategoryUseCase)
                .deleteById(unknownId);

        mockMvc.perform(delete("/api/categories/{id}", unknownId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("CATEGORY_NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn226WhenDeletingCategoryInUse() throws Exception {
        UUID categoryId = UUID.randomUUID();

        doThrow(new CategoryInUseException(categoryId))
                .when(deleteCategoryUseCase)
                .deleteById(categoryId);

        mockMvc.perform(delete("/api/categories/{id}", categoryId))
                .andExpect(status().isImUsed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("CATEGORY_IN_USE"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn200WhenCategoryIsUpdated() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "Groceries",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );

        Category updated = new Category(
                categoryId,
                userId,
                "Groceries",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );

        when(updateCategoryUseCase.update(eq(categoryId), any(UpdateCategoryCommand.class)))
                .thenReturn(updated);

        // Act + Assert
        mockMvc.perform(put("/api/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(categoryId.toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Groceries"))
                .andExpect(jsonPath("$.kind").value("EXPENSE"))
                .andExpect(jsonPath("$.color.name").value("ORANGE"))
                .andExpect(jsonPath("$.color.hex").value("#F97316"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingCategory() throws Exception {
        // Arrange
        UUID unknownId = UUID.randomUUID();

        UpdateCategoryRequest request = new UpdateCategoryRequest(
                "Whatever",
                CategoryKind.EXPENSE,
                CategoryColor.ORANGE
        );

        when(updateCategoryUseCase.update(eq(unknownId), any(UpdateCategoryCommand.class)))
                .thenThrow(new CategoryNotFoundException(unknownId));

        // Act + Assert
        mockMvc.perform(put("/api/categories/{id}", unknownId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("CATEGORY_NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }
}
