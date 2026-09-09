package com.representative.representative_service.api.controllers;

import com.representative.representative_service.api.responses.CreateRepresentativeResponse;
import com.representative.representative_service.api.responses.GetRepresentativeResponse;
import com.representative.representative_service.api.responses.ListRepresentativeResponse;
import com.representative.representative_service.api.responses.PaginatedRepresentativeResponse;
import com.representative.representative_service.api.responses.Pagination;
import com.representative.representative_service.api.schemas.CreateRepresentativeSchema;
import com.representative.representative_service.application.services.RepresentativeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/representatives")
public class RepresentativeController {

    private final RepresentativeService representativeService;

    public RepresentativeController(RepresentativeService representativeService) {
        this.representativeService = representativeService;
    }

    @PostMapping
    public ResponseEntity<CreateRepresentativeResponse> createRepresentative(@Valid @RequestBody CreateRepresentativeSchema request) {
        var dto = request.toDTO();

        var representative = representativeService.createRepresentative(dto);
        var response = CreateRepresentativeResponse.fromEntity(representative);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<GetRepresentativeResponse> getByCpf(@PathVariable String cpf) {
        var representative = representativeService.getRepresentativeByCpf(cpf);
        var response = GetRepresentativeResponse.fromEntity(representative);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedRepresentativeResponse> listRepresentatives(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "30") Integer pageSize
    ) {
        var representatives = representativeService.listRepresentatives(name, page, pageSize);
        var data = representatives.stream()
            .map(ListRepresentativeResponse::fromEntity)
            .toList();
        var pagination = new Pagination(page, pageSize);
        var response = new PaginatedRepresentativeResponse(data, pagination);
        return ResponseEntity.status(200).body(response);
    }
}
