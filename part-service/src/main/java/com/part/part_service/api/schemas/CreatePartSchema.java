package com.part.part_service.api.schemas;

import com.part.part_service.application.dtos.input.CreatePartDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreatePartSchema {

    @NotBlank
    @Size(max = 50)
    private String identificationNumber;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CreatePartDTO toDTO() {
        return new CreatePartDTO(
            this.identificationNumber,
            this.name,
            this.description
        );
    }
}
