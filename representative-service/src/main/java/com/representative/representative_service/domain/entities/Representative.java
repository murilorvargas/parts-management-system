package com.representative.representative_service.domain.entities;

import java.time.LocalDateTime;

public class Representative {

    private Long id;
    private String publicKey;
    private String cpf;
    private String name;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public Representative() {
    }

    public Representative(
        String publicKey,
        String cpf,
        String name
    ) {
        this.publicKey = publicKey;
        this.cpf = cpf;
        this.name = name;
    }

    public Representative(
        Long id,
        String publicKey,
        String cpf,
        String name,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
    ) {
        this(publicKey, cpf, name);
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
