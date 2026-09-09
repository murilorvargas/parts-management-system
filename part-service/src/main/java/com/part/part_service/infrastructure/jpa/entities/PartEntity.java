package com.part.part_service.infrastructure.jpa.entities;

import com.part.part_service.domain.entities.Part;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Part")
public class PartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_key", nullable = false, unique = true, length = 36)
    private String publicKey;

    @Column(name = "identification_number", nullable = false, unique = true, length = 50)
    private String identificationNumber;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public PartEntity() {
    }

    public PartEntity(
        Long id,
        String publicKey,
        String identificationNumber,
        String name,
        String description,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
    ) {
        this.id = id;
        this.publicKey = publicKey;
        this.identificationNumber = identificationNumber;
        this.name = name;
        this.description = description;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public static PartEntity fromDomain(Part domain) {
        return new PartEntity(
            domain.getId(),
            domain.getPublicKey(),
            domain.getIdentificationNumber(),
            domain.getName(),
            domain.getDescription(),
            domain.getUpdatedAt(),
            domain.getCreatedAt()
        );
    }

    public Part toDomain() {
        return new Part(
            this.id,
            this.publicKey,
            this.identificationNumber,
            this.name,
            this.description,
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
