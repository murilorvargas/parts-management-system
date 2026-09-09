package com.part.part_service.infrastructure.jpa.repositories;

import com.part.part_service.domain.entities.Part;
import com.part.part_service.domain.repositories.PartRepository;
import com.part.part_service.infrastructure.jpa.entities.PartEntity;
import com.part.part_service.infrastructure.jpa.specifications.PartSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PartRepositoryImpl implements PartRepository {

    private final JpaPartRepository jpaRepository;

    public PartRepositoryImpl(JpaPartRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Part save(Part part) {
        var entity = PartEntity.fromDomain(part);
        var saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Part> findByIdentificationNumber(String identificationNumber) {
        return jpaRepository.findByIdentificationNumber(identificationNumber)
            .map(PartEntity::toDomain);
    }

    @Override
    public List<Part> findAll(String name, int page, int pageSize) {
        var spec = PartSpecification.withFilters(name);
        var pageable = PageRequest.of(page - 1, pageSize);
        return jpaRepository.findAll(spec, pageable)
            .map(PartEntity::toDomain)
            .getContent();
    }
}
