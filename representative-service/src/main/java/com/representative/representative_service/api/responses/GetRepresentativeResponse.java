package com.representative.representative_service.api.responses;

import com.representative.representative_service.domain.entities.Representative;

public class GetRepresentativeResponse {

    private String publicKey;
    private String cpf;
    private String name;

    public GetRepresentativeResponse() {
    }

    public GetRepresentativeResponse(
            String publicKey,
            String cpf,
            String name
    ) {
        this.publicKey = publicKey;
        this.cpf = cpf;
        this.name = name;
    }

    public static GetRepresentativeResponse fromEntity(Representative entity) {
        return new GetRepresentativeResponse(
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
