package dev.jossegonnza.personal_finance_manager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateCategoryRequest;
import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.exception.CategoryNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.exception.UserNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateCategoryCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetCategoryUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
import dev.jossegonnza.personal_finance_manager.domain.model.Category;
import dev.jossegonnza.personal_finance_manager.domain.model.CategoryKind;
import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void shouldReturn201WhenCreateCategory() throws Exception {
        //Arrange
        UUID userId = UUID.randomUUID();
        CreateCategoryRequest request = new CreateCategoryRequest(
                userId,
                "Restaurantes",
                CategoryKind.EXPENSE,
                "Purple"
        );

        Category category = new Category(
                userId,
                "Restaurantes",
                CategoryKind.EXPENSE,
                "Purple"
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
                .andExpect(jsonPath("$.colorHex").value("Purple"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws UserNotFoundException, Exception {
        //Arrange
        UUID userId = UUID.randomUUID();

        CreateCategoryRequest request = new CreateCategoryRequest(
                userId,
                "Restaurantes",
                CategoryKind.EXPENSE,
                "Purple"
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
                "Verde"
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
                .andExpect(jsonPath("$.colorHex").value("Verde"));
    }

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
}
