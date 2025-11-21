package dev.jossegonnza.personal_finance_manager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateAccountRequest;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.Account;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;

    @Test
    void shouldReturn201WhenCreateAccount() throws Exception {
        //Arrange
        UUID userId = UUID.randomUUID();

        CreateAccountRequest request = new CreateAccountRequest(
                userId,
                "Personal",
                CurrencyType.EUR
        );

        Account account = new Account(
                userId,
                "Personal",
                CurrencyType.EUR
        );

        when(createAccountUseCase.create(any(CreateAccountCommand.class)))
                .thenReturn(account);

        //Act + Assert
        mockMvc.perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(account.id().toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Personal"))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.balance").value(0.00));
    }
}
