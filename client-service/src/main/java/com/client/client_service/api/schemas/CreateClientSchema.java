package com.client.client_service.api.schemas;

import com.client.client_service.application.dtos.input.CreateClientDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateClientSchema {

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "must contain exactly 11 digits")
    private String cpf;

    @NotBlank
    @Size(max = 255)
    private String name;

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public CreateClientDTO toDTO() {
        return new CreateClientDTO(
            this.cpf,
            this.name
        );
    }
}
