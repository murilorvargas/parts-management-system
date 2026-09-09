package com.client.client_service.application.services;

import com.client.client_service.application.dtos.input.CreateClientDTO;
import com.client.client_service.domain.UnitOfWork;
import com.client.client_service.domain.entities.Client;
import com.client.client_service.domain.exceptions.ClientAlreadyExistsException;
import com.client.client_service.domain.exceptions.ClientNotFoundException;
import com.client.client_service.domain.repositories.ClientRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ClientService.class);

    private final UnitOfWork unitOfWork;
    private final ClientRepository clientRepository;

    public ClientService(
            UnitOfWork unitOfWork,
            ClientRepository clientRepository
    ) {
        this.unitOfWork = unitOfWork;
        this.clientRepository = clientRepository;
    }

    public Client createClient(CreateClientDTO dto) {
        logger.info("ClientService.createClient - Starting");

        if (clientRepository.findByCpf(dto.cpf()).isPresent()) {
            throw new ClientAlreadyExistsException();
        }

        unitOfWork.begin();
        Client client = clientRepository.save(new Client(
            UUID.randomUUID().toString(),
            dto.cpf(),
            dto.name()
        ));
        unitOfWork.commit();

        logger.info("ClientService.createClient - Successfully finished");
        return client;
    }

    public Client getClientByCpf(String cpf) {
        return clientRepository.findByCpf(cpf)
            .orElseThrow(ClientNotFoundException::new);
    }

    public List<Client> listClients(String name, int page, int pageSize) {
        return clientRepository.findAll(name, page, pageSize);
    }
}
