package com.part.part_service.api.responses;

import com.part.part_service.domain.entities.Part;

public class ListPartResponse {

    private String publicKey;
    private String identificationNumber;
    private String name;
    private String description;

    public ListPartResponse() {
    }

    public ListPartResponse(
            String publicKey,
            String identificationNumber,
            String name,
            String description
    ) {
        this.publicKey = publicKey;
        this.identificationNumber = identificationNumber;
        this.name = name;
        this.description = description;
    }

    public static ListPartResponse fromEntity(Part entity) {
        return new ListPartResponse(
                entity.getPublicKey(),
                entity.getIdentificationNumber(),
                entity.getName(),
                entity.getDescription()
        );
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
