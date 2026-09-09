package com.client.client_service.api.responses;

import com.client.client_service.domain.entities.Client;

public class ListClientResponse {

    private String publicKey;
    private String cpf;
    private String name;

    public ListClientResponse() {
    }

    public ListClientResponse(
            String publicKey,
            String cpf,
            String name
    ) {
        this.publicKey = publicKey;
        this.cpf = cpf;
        this.name = name;
    }

    public static ListClientResponse fromEntity(Client entity) {
        return new ListClientResponse(
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
