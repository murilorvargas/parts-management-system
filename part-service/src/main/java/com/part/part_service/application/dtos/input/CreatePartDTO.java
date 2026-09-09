package com.part.part_service.application.dtos.input;

public record CreatePartDTO(
    String identificationNumber,
    String name,
    String description
) {
}
