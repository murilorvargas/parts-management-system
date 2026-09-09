package com.client.client_service.domain.repositories;

import com.client.client_service.domain.entities.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    Client save(Client client);

    Optional<Client> findByCpf(String cpf);

    List<Client> findAll(String name, int page, int pageSize);
}
