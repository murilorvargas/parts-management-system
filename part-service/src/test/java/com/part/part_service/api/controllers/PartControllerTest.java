package com.part.part_service.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.part.part_service.application.dtos.input.CreatePartDTO;
import com.part.part_service.application.services.PartService;
import com.part.part_service.domain.entities.Part;
import com.part.part_service.domain.exceptions.PartAlreadyExistsException;
import com.part.part_service.domain.exceptions.PartNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartController.class)
class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PartService partService;

    private Part samplePart() {
        return new Part(
            1L,
            "11111111-1111-1111-1111-111111111111",
            "IDN-001",
            "Filtro de oleo",
            "Filtro de oleo original",
            null,
            null
        );
    }

    @Test
    void createPart_shouldReturn201_whenValidRequest() throws Exception {
        when(partService.createPart(any(CreatePartDTO.class))).thenReturn(samplePart());

        var payload = """
            {
                "identificationNumber": "IDN-001",
                "name": "Filtro de oleo",
                "description": "Filtro de oleo original"
            }
            """;

        mockMvc.perform(post("/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.publicKey").value("11111111-1111-1111-1111-111111111111"))
            .andExpect(jsonPath("$.identificationNumber").value("IDN-001"))
            .andExpect(jsonPath("$.name").value("Filtro de oleo"))
            .andExpect(jsonPath("$.description").value("Filtro de oleo original"));
    }

    @Test
    void createPart_shouldReturn400_whenIdentificationNumberIsBlank() throws Exception {
        var payload = """
            {
                "identificationNumber": "",
                "name": "Filtro de oleo"
            }
            """;

        mockMvc.perform(post("/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("GEN00400"));
    }

    @Test
    void createPart_shouldReturn409_whenPartAlreadyExists() throws Exception {
        when(partService.createPart(any(CreatePartDTO.class))).thenThrow(new PartAlreadyExistsException());

        var payload = """
            {
                "identificationNumber": "IDN-001",
                "name": "Filtro de oleo"
            }
            """;

        mockMvc.perform(post("/parts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("PRT00002"))
            .andExpect(jsonPath("$.message").value("A part with this identification number already exists."));
    }

    @Test
    void getByIdentificationNumber_shouldReturn200_whenFound() throws Exception {
        when(partService.getPartByIdentificationNumber("IDN-001")).thenReturn(samplePart());

        mockMvc.perform(get("/parts/{identificationNumber}", "IDN-001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.identificationNumber").value("IDN-001"))
            .andExpect(jsonPath("$.name").value("Filtro de oleo"));
    }

    @Test
    void getByIdentificationNumber_shouldReturn404_whenNotFound() throws Exception {
        when(partService.getPartByIdentificationNumber("UNKNOWN")).thenThrow(new PartNotFoundException());

        mockMvc.perform(get("/parts/{identificationNumber}", "UNKNOWN"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("PRT00001"))
            .andExpect(jsonPath("$.message").value("Part not found. Please check the provided information and try again."));
    }

    @Test
    void listParts_shouldReturn200_withPaginatedShapeAndDefaults() throws Exception {
        when(partService.listParts(eq(null), anyInt(), anyInt())).thenReturn(List.of(samplePart()));

        mockMvc.perform(get("/parts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].identificationNumber").value("IDN-001"))
            .andExpect(jsonPath("$.data[0].name").value("Filtro de oleo"))
            .andExpect(jsonPath("$.pagination.page").value(1))
            .andExpect(jsonPath("$.pagination.pageSize").value(30));

        verify(partService).listParts(null, 1, 30);
    }

    @Test
    void listParts_shouldReturn200_withNameFilterAndCustomPagination() throws Exception {
        when(partService.listParts(eq("Filtro"), anyInt(), anyInt())).thenReturn(List.of(samplePart()));

        mockMvc.perform(get("/parts")
                .param("name", "Filtro")
                .param("page", "2")
                .param("pageSize", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.pagination.page").value(2))
            .andExpect(jsonPath("$.pagination.pageSize").value(10));

        verify(partService).listParts("Filtro", 2, 10);
    }
}
