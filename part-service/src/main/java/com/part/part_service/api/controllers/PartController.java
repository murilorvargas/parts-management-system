package com.part.part_service.api.controllers;

import com.part.part_service.api.responses.CreatePartResponse;
import com.part.part_service.api.responses.GetPartResponse;
import com.part.part_service.api.responses.ListPartResponse;
import com.part.part_service.api.responses.PaginatedPartResponse;
import com.part.part_service.api.responses.Pagination;
import com.part.part_service.api.schemas.CreatePartSchema;
import com.part.part_service.application.services.PartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @PostMapping
    public ResponseEntity<CreatePartResponse> createPart(@Valid @RequestBody CreatePartSchema request) {
        var dto = request.toDTO();

        var part = partService.createPart(dto);
        var response = CreatePartResponse.fromEntity(part);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{identificationNumber}")
    public ResponseEntity<GetPartResponse> getByIdentificationNumber(@PathVariable String identificationNumber) {
        var part = partService.getPartByIdentificationNumber(identificationNumber);
        var response = GetPartResponse.fromEntity(part);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedPartResponse> listParts(
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "30") Integer pageSize
    ) {
        var parts = partService.listParts(name, page, pageSize);
        var data = parts.stream()
            .map(ListPartResponse::fromEntity)
            .toList();
        var pagination = new Pagination(page, pageSize);
        var response = new PaginatedPartResponse(data, pagination);
        return ResponseEntity.status(200).body(response);
    }
}
