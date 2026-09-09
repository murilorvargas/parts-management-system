package com.client.client_service.infrastructure.jpa.repositories;

import com.client.client_service.infrastructure.jpa.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface JpaClientRepository extends JpaRepository<ClientEntity, Long>, JpaSpecificationExecutor<ClientEntity> {

    Optional<ClientEntity> findByCpf(String cpf);

}
