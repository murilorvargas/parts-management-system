package com.client.client_service.api.responses;

import com.client.client_service.domain.entities.Client;

public class CreateClientResponse {

    private String publicKey;
    private String cpf;
    private String name;

    public CreateClientResponse() {
    }

    public CreateClientResponse(
            String publicKey,
            String cpf,
            String name
    ) {
        this.publicKey = publicKey;
        this.cpf = cpf;
        this.name = name;
    }

    public static CreateClientResponse fromEntity(Client entity) {
        return new CreateClientResponse(
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
