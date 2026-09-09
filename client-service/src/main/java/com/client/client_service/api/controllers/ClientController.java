package com.client.client_service.api.controllers;

import com.client.client_service.api.responses.CreateClientResponse;
import com.client.client_service.api.responses.GetClientResponse;
import com.client.client_service.api.responses.ListClientResponse;
import com.client.client_service.api.responses.PaginatedClientResponse;
import com.client.client_service.api.responses.Pagination;
import com.client.client_service.api.schemas.CreateClientSchema;
import com.client.client_service.application.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<CreateClientResponse> createClient(@Valid @RequestBody CreateClientSchema request) {
        var dto = request.toDTO();

        var client = clientService.createClient(dto);
        var response = CreateClientResponse.fromEntity(client);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<GetClientResponse> getByCpf(@PathVariable String cpf) {
        var client = clientService.getClientByCpf(cpf);
        var response = GetClientResponse.fromEntity(client);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedClientResponse> listClients(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "30") Integer pageSize
    ) {
        var clients = clientService.listClients(name, page, pageSize);
        var data = clients.stream()
            .map(ListClientResponse::fromEntity)
            .toList();
        var pagination = new Pagination(page, pageSize);
        var response = new PaginatedClientResponse(data, pagination);
        return ResponseEntity.status(200).body(response);
    }
}
