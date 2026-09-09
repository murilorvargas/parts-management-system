package com.client.client_service.infrastructure.jpa.entities;

import com.client.client_service.domain.entities.Client;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_key", nullable = false, unique = true, length = 36)
    private String publicKey;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public ClientEntity() {
    }

    public ClientEntity(
        Long id,
        String publicKey,
        String cpf,
        String name,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.publicKey = publicKey;
        this.cpf = cpf;
        this.name = name;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public static ClientEntity fromDomain(Client domain) {
        return new ClientEntity(
            domain.getId(),
            domain.getPublicKey(),
            domain.getCpf(),
            domain.getName(),
            domain.getUpdatedAt(),
            domain.getCreatedAt()
        );
    }

    public Client toDomain() {
        return new Client(
            this.id,
            this.publicKey,
            this.cpf,
            this.name,
            this.updatedAt,
            this.createdAt
        );
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
