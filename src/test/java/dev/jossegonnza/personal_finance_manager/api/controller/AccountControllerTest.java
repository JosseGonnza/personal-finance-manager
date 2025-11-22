package dev.jossegonnza.personal_finance_manager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jossegonnza.personal_finance_manager.api.dto.CreateAccountRequest;
import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.command.CreateAccountUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountTransactionsUseCase;
import dev.jossegonnza.personal_finance_manager.application.port.in.query.GetAccountUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;

    @MockitoBean
    private GetAccountUseCase getAccountUseCase;

    @MockitoBean
    private GetAccountTransactionsUseCase getAccountTransactionsUseCase;

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

    @Test
    void shouldReturn200WhenAccountExist() throws Exception {
        //Arrange
        UUID userId = UUID.randomUUID();
        Account account = new Account(
                userId,
                "Personal",
                CurrencyType.EUR
        );

        when(getAccountUseCase.getById(account.id()))
                .thenReturn(account);

        //Act + Assert
        mockMvc.perform(get("/api/accounts/{id}", account.id()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(account.id().toString()))
                .andExpect(jsonPath("$.userId").value(account.userId().toString()))
                .andExpect(jsonPath("$.name").value("Personal"))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.balance").value(0.00));
    }

    @Test
    void shouldReturn404WhenAccountNotFound() throws Exception {
        // Arrange
        UUID unknownId = UUID.randomUUID();

        when(getAccountUseCase.getById(unknownId))
                .thenThrow(new AccountNotFoundException(unknownId));

        // Act + Assert
        mockMvc.perform(get("/api/accounts/{id}", unknownId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("ACCOUNT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn200WhenAccountHasTransactions() throws Exception {
        //Arrange
        UUID accountId = UUID.randomUUID();
        UUID categoryId1 = UUID.randomUUID();
        UUID categoryId2 = UUID.randomUUID();

        Transaction transaction1 = new Transaction(
                accountId,
                TransactionType.INCOME,
                new Money(new BigDecimal("50.00"), CurrencyType.EUR),
                categoryId1,
                "Extras",
                LocalDateTime.of(2025, 2, 8, 10, 30)
        );
        Transaction transaction2 = new Transaction(
                accountId,
                TransactionType.EXPENSE,
                new Money(new BigDecimal("25.00"), CurrencyType.EUR),
                categoryId2,
                "Dinner",
                LocalDateTime.of(2025, 2, 8, 11, 30)
        );

        when(getAccountTransactionsUseCase.getByAccountId(accountId))
                .thenReturn(List.of(transaction1, transaction2));

        //Act + Assert
        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$[0].id").value(transaction1.id().toString()))
                .andExpect(jsonPath("$[0].accountId").value(accountId.toString()))
                .andExpect(jsonPath("$[0].type").value("INCOME"))
                .andExpect(jsonPath("$[0].amount").value(50.00))
                .andExpect(jsonPath("$[0].currency").value("EUR"))
                .andExpect(jsonPath("$[0].categoryId").value(categoryId1.toString()))
                .andExpect(jsonPath("$[0].description").value("Extras"))
                .andExpect(jsonPath("$[0].occurredAt").value("2025-02-08T10:30:00"))

                .andExpect(jsonPath("$[1].id").value(transaction2.id().toString()))
                .andExpect(jsonPath("$[1].accountId").value(accountId.toString()))
                .andExpect(jsonPath("$[1].type").value("EXPENSE"))
                .andExpect(jsonPath("$[1].amount").value(25.00))
                .andExpect(jsonPath("$[1].currency").value("EUR"))
                .andExpect(jsonPath("$[1].categoryId").value(categoryId2.toString()))
                .andExpect(jsonPath("$[1].description").value("Dinner"))
                .andExpect(jsonPath("$[1].occurredAt").value("2025-02-08T11:30:00"));
    }

    @Test
    void shouldReturn200WithEmptyArrayWhenAccountHasNoTransactions() throws Exception {
        // Arrange
        UUID accountId = UUID.randomUUID();

        when(getAccountTransactionsUseCase.getByAccountId(accountId))
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}
