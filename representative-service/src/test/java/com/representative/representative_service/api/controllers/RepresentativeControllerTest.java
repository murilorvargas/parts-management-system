package com.representative.representative_service.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.representative.representative_service.application.services.RepresentativeService;
import com.representative.representative_service.domain.entities.Representative;
import com.representative.representative_service.domain.exceptions.RepresentativeAlreadyExistsException;
import com.representative.representative_service.domain.exceptions.RepresentativeNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepresentativeController.class)
class RepresentativeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RepresentativeService representativeService;

    private Representative buildRepresentative(String cpf, String name) {
        return new Representative(
            1L,
            UUID.randomUUID().toString(),
            cpf,
            name,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void createRepresentative_shouldReturn201_whenPayloadIsValid() throws Exception {
        var representative = buildRepresentative("12345678901", "John Doe");
        when(representativeService.createRepresentative(any())).thenReturn(representative);

        var payload = """
            {
              "cpf": "12345678901",
              "name": "John Doe"
            }
            """;

        mockMvc.perform(post("/representatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.publicKey").value(representative.getPublicKey()))
            .andExpect(jsonPath("$.cpf").value("12345678901"))
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void createRepresentative_shouldReturn400_whenCpfIsInvalid() throws Exception {
        var payload = """
            {
              "cpf": "123",
              "name": "John Doe"
            }
            """;

        mockMvc.perform(post("/representatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("GEN00400"));
    }

    @Test
    void createRepresentative_shouldReturn409_whenRepresentativeAlreadyExists() throws Exception {
        when(representativeService.createRepresentative(any()))
            .thenThrow(new RepresentativeAlreadyExistsException());

        var payload = """
            {
              "cpf": "12345678901",
              "name": "John Doe"
            }
            """;

        mockMvc.perform(post("/representatives")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("REP00002"))
            .andExpect(jsonPath("$.message").value("A representative with this CPF already exists."));
    }

    @Test
    void getByCpf_shouldReturn200_whenRepresentativeIsFound() throws Exception {
        var representative = buildRepresentative("12345678901", "John Doe");
        when(representativeService.getRepresentativeByCpf("12345678901")).thenReturn(representative);

        mockMvc.perform(get("/representatives/{cpf}", "12345678901"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cpf").value("12345678901"))
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getByCpf_shouldReturn404_whenRepresentativeIsNotFound() throws Exception {
        when(representativeService.getRepresentativeByCpf(anyString()))
            .thenThrow(new RepresentativeNotFoundException());

        mockMvc.perform(get("/representatives/{cpf}", "99999999999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("REP00001"))
            .andExpect(jsonPath("$.message").value("Representative not found. Please check the provided information and try again."));
    }

    @Test
    void listRepresentatives_shouldReturn200_withPaginatedShapeAndDefaults() throws Exception {
        var representative = buildRepresentative("12345678901", "John Doe");
        when(representativeService.listRepresentatives(any(), anyInt(), anyInt()))
            .thenReturn(List.of(representative));

        mockMvc.perform(get("/representatives"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].cpf").value("12345678901"))
            .andExpect(jsonPath("$.data[0].name").value("John Doe"))
            .andExpect(jsonPath("$.pagination.page").value(1))
            .andExpect(jsonPath("$.pagination.pageSize").value(30));
    }
}
