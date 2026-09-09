package com.representative.representative_service.api.responses;

import com.representative.representative_service.domain.entities.Representative;

public class CreateRepresentativeResponse {

    private String publicKey;
    private String cpf;
    private String name;

    public CreateRepresentativeResponse() {
    }

    public CreateRepresentativeResponse(
            String publicKey,
            String cpf,
            String name
    ) {
        this.publicKey = publicKey;
        this.cpf = cpf;
        this.name = name;
    }

    public static CreateRepresentativeResponse fromEntity(Representative entity) {
        return new CreateRepresentativeResponse(
                entity.getPublicKey(),
                entity.getCpf(),
                entity.getName()
        );
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }
}
