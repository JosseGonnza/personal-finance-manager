package dev.jossegonnza.personal_finance_manager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jossegonnza.personal_finance_manager.api.dto.RegisterTransactionRequest;
import dev.jossegonnza.personal_finance_manager.application.exception.AccountNotFoundException;
import dev.jossegonnza.personal_finance_manager.application.port.in.RegisterTransactionCommand;
import dev.jossegonnza.personal_finance_manager.application.port.in.RegisterTransactionUseCase;
import dev.jossegonnza.personal_finance_manager.domain.model.CurrencyType;
import dev.jossegonnza.personal_finance_manager.domain.model.Money;
import dev.jossegonnza.personal_finance_manager.domain.model.Transaction;
import dev.jossegonnza.personal_finance_manager.domain.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegisterTransactionUseCase registerTransactionUseCase;

    @Test
    void shouldReturn201WhenCreateTransaction() throws AccountNotFoundException, Exception {
        //Arrange
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        RegisterTransactionRequest request = new RegisterTransactionRequest(
                accountId,
                TransactionType.INCOME,
                new BigDecimal("1000.00"),
                CurrencyType.EUR,
                categoryId,
                "Salary",
                LocalDateTime.of(2025, 2, 8, 10, 30)
        );

        Money money = new Money(new BigDecimal("1000.00"), CurrencyType.EUR);
        Transaction transaction = new Transaction(
                accountId,
                TransactionType.INCOME,
                money,
                categoryId,
                "Salary",
                LocalDateTime.of(2025, 2, 8, 10, 30)
        );

        when(registerTransactionUseCase.register(any(RegisterTransactionCommand.class)))
                .thenReturn(transaction);

        String jsonBody = objectMapper.writeValueAsString(request);

        //Act + Assert
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(transaction.id().toString()))
                .andExpect(jsonPath("$.accountId").value(accountId.toString()))
                .andExpect(jsonPath("$.type").value("INCOME"))
                .andExpect(jsonPath("$.amount").value(1000.00))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andExpect(jsonPath("$.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.description").value("Salary"))
                .andExpect(jsonPath("$.occurredAt").value("2025-02-08T10:30:00"));
    }

    @Test
    void shouldReturn404WhenAccountNotFound() throws AccountNotFoundException, Exception {
        //Arrange
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        RegisterTransactionRequest request = new RegisterTransactionRequest(
                accountId,
                TransactionType.INCOME,
                new BigDecimal("1000.00"),
                CurrencyType.EUR,
                categoryId,
                "Salary",
                LocalDateTime.of(2025, 2, 8, 10, 30)
        );

        when(registerTransactionUseCase.register(any(RegisterTransactionCommand.class)))
                .thenThrow(new AccountNotFoundException(accountId));

        //Act + Assert
        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("ACCOUNT_NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }
}
