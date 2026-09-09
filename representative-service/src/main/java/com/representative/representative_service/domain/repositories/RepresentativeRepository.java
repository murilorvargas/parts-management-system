package com.representative.representative_service.domain.repositories;

import com.representative.representative_service.domain.entities.Representative;

import java.util.List;
import java.util.Optional;

public interface RepresentativeRepository {

    Representative save(Representative representative);

    Optional<Representative> findByCpf(String cpf);

    List<Representative> findAll(String name, int page, int pageSize);
}
