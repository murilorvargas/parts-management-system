package com.client.client_service.api.responses;

import com.client.client_service.domain.entities.Client;

public class GetClientResponse {

    private String publicKey;
    private String cpf;
    private String name;

    public GetClientResponse() {
    }

    public GetClientResponse(
            String publicKey,
            String cpf,
            String name
    ) {
        this.publicKey = publicKey;
        this.cpf = cpf;
        this.name = name;
    }

    public static GetClientResponse fromEntity(Client entity) {
        return new GetClientResponse(
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
