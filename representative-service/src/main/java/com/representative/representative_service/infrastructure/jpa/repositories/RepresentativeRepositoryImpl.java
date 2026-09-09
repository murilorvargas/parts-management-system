package com.representative.representative_service.infrastructure.jpa.repositories;

import com.representative.representative_service.domain.entities.Representative;
import com.representative.representative_service.domain.repositories.RepresentativeRepository;
import com.representative.representative_service.infrastructure.jpa.entities.RepresentativeEntity;
import com.representative.representative_service.infrastructure.jpa.specifications.RepresentativeSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RepresentativeRepositoryImpl implements RepresentativeRepository {

    private final JpaRepresentativeRepository jpaRepository;

    public RepresentativeRepositoryImpl(JpaRepresentativeRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Representative save(Representative representative) {
        var entity = RepresentativeEntity.fromDomain(representative);
        var saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Representative> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf)
            .map(RepresentativeEntity::toDomain);
    }

    @Override
    public List<Representative> findAll(String name, int page, int pageSize) {
        var spec = RepresentativeSpecification.withFilters(name);
        var pageable = PageRequest.of(page - 1, pageSize);
        return jpaRepository.findAll(spec, pageable)
            .map(RepresentativeEntity::toDomain)
            .getContent();
    }
}
