package com.part.part_service.domain.entities;

import java.time.LocalDateTime;

public class Part {

    private Long id;
    private String publicKey;
    private String identificationNumber;
    private String name;
    private String description;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public Part() {
    }

    public Part(
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

    public Part(
        Long id,
        String publicKey,
        String identificationNumber,
        String name,
        String description,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
    ) {
        this(publicKey, identificationNumber, name, description);
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
