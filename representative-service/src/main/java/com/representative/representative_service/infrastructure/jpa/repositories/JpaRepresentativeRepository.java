package com.representative.representative_service.infrastructure.jpa.repositories;

import com.representative.representative_service.infrastructure.jpa.entities.RepresentativeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JpaRepresentativeRepository extends JpaRepository<RepresentativeEntity, Long>, JpaSpecificationExecutor<RepresentativeEntity> {

    Optional<RepresentativeEntity> findByCpf(String cpf);

}
