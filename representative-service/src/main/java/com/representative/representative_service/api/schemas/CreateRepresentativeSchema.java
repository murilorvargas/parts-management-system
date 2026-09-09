package com.representative.representative_service.api.schemas;

import com.representative.representative_service.application.dtos.input.CreateRepresentativeDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateRepresentativeSchema {

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

    public CreateRepresentativeDTO toDTO() {
        return new CreateRepresentativeDTO(
            this.cpf,
            this.name
        );
    }
}
