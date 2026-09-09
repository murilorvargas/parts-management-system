package com.representative.representative_service.infrastructure.jpa.entities;

import com.representative.representative_service.domain.entities.Representative;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Representative")
public class RepresentativeEntity {

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

    public RepresentativeEntity() {
    }

    public RepresentativeEntity(
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

    public static RepresentativeEntity fromDomain(Representative domain) {
        return new RepresentativeEntity(
            domain.getId(),
            domain.getPublicKey(),
            domain.getCpf(),
            domain.getName(),
            domain.getUpdatedAt(),
            domain.getCreatedAt()
        );
    }

    public Representative toDomain() {
        return new Representative(
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
