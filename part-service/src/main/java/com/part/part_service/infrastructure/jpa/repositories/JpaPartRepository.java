package com.part.part_service.infrastructure.jpa.repositories;

import com.part.part_service.infrastructure.jpa.entities.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JpaPartRepository extends JpaRepository<PartEntity, Long>, JpaSpecificationExecutor<PartEntity> {

    Optional<PartEntity> findByIdentificationNumber(String identificationNumber);

}
