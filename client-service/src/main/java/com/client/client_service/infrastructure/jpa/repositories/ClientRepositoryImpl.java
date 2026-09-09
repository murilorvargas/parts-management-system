package com.client.client_service.infrastructure.jpa.repositories;

import com.client.client_service.domain.entities.Client;
import com.client.client_service.domain.repositories.ClientRepository;
import com.client.client_service.infrastructure.jpa.entities.ClientEntity;
import com.client.client_service.infrastructure.jpa.specifications.ClientSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final JpaClientRepository jpaRepository;

    public ClientRepositoryImpl(JpaClientRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Client save(Client client) {
        var entity = ClientEntity.fromDomain(client);
        var saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Client> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf)
            .map(ClientEntity::toDomain);
    }

    @Override
    public List<Client> findAll(String name, int page, int pageSize) {
        var spec = ClientSpecification.withFilters(name);
        var pageable = PageRequest.of(page - 1, pageSize);
        return jpaRepository.findAll(spec, pageable)
            .map(ClientEntity::toDomain)
            .getContent();
    }
}
