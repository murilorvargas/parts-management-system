package com.client.client_service.api.controllers;

import com.client.client_service.application.services.ClientService;
import com.client.client_service.domain.entities.Client;
import com.client.client_service.domain.exceptions.ClientAlreadyExistsException;
import com.client.client_service.domain.exceptions.ClientNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    private Client buildClient() {
        return new Client(
            1L,
            "11111111-1111-1111-1111-111111111111",
            "12345678901",
            "John Doe",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void createClient_shouldReturn201_whenRequestIsValid() throws Exception {
        var client = buildClient();
        when(clientService.createClient(any())).thenReturn(client);

        var payload = """
            {
                "cpf": "12345678901",
                "name": "John Doe"
            }
            """;

        mockMvc.perform(post("/clients")
                .contentType("application/json")
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.publicKey").value(client.getPublicKey()))
            .andExpect(jsonPath("$.cpf").value(client.getCpf()))
            .andExpect(jsonPath("$.name").value(client.getName()));
    }

    @Test
    void createClient_shouldReturn400_whenCpfIsInvalid() throws Exception {
        var payload = """
            {
                "cpf": "123",
                "name": "John Doe"
            }
            """;

        mockMvc.perform(post("/clients")
                .contentType("application/json")
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("GEN00400"));
    }

    @Test
    void createClient_shouldReturn409_whenClientAlreadyExists() throws Exception {
        when(clientService.createClient(any())).thenThrow(new ClientAlreadyExistsException());

        var payload = """
            {
                "cpf": "12345678901",
                "name": "John Doe"
            }
            """;

        mockMvc.perform(post("/clients")
                .contentType("application/json")
                .content(payload))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("CLI00002"))
            .andExpect(jsonPath("$.message").value("A client with this CPF already exists."));
    }

    @Test
    void getByCpf_shouldReturn200_whenClientIsFound() throws Exception {
        var client = buildClient();
        when(clientService.getClientByCpf(eq("12345678901"))).thenReturn(client);

        mockMvc.perform(get("/clients/{cpf}", "12345678901"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.publicKey").value(client.getPublicKey()))
            .andExpect(jsonPath("$.cpf").value(client.getCpf()))
            .andExpect(jsonPath("$.name").value(client.getName()));
    }

    @Test
    void getByCpf_shouldReturn404_whenClientIsNotFound() throws Exception {
        when(clientService.getClientByCpf(eq("00000000000"))).thenThrow(new ClientNotFoundException());

        mockMvc.perform(get("/clients/{cpf}", "00000000000"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("CLI00001"))
            .andExpect(jsonPath("$.message").value("Client not found. Please check the provided information and try again."));
    }

    @Test
    void listClients_shouldReturn200WithPaginatedShapeAndDefaults_whenQueryParamsAreOmitted() throws Exception {
        var client = buildClient();
        when(clientService.listClients(eq(null), eq(1), eq(30))).thenReturn(List.of(client));

        mockMvc.perform(get("/clients"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].publicKey").value(client.getPublicKey()))
            .andExpect(jsonPath("$.data[0].cpf").value(client.getCpf()))
            .andExpect(jsonPath("$.data[0].name").value(client.getName()))
            .andExpect(jsonPath("$.pagination.page").value(1))
            .andExpect(jsonPath("$.pagination.pageSize").value(30));
    }

    @Test
    void listClients_shouldReturn200WithProvidedPageAndPageSize_whenQueryParamsArePresent() throws Exception {
        when(clientService.listClients(eq("John"), anyInt(), anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/clients")
                .param("name", "John")
                .param("page", "2")
                .param("pageSize", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.pagination.page").value(2))
            .andExpect(jsonPath("$.pagination.pageSize").value(10));
    }
}
