package com.representative.representative_service.application.services;

import com.representative.representative_service.application.dtos.input.CreateRepresentativeDTO;
import com.representative.representative_service.domain.UnitOfWork;
import com.representative.representative_service.domain.entities.Representative;
import com.representative.representative_service.domain.exceptions.RepresentativeAlreadyExistsException;
import com.representative.representative_service.domain.exceptions.RepresentativeNotFoundException;
import com.representative.representative_service.domain.repositories.RepresentativeRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RepresentativeService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(RepresentativeService.class);

    private final UnitOfWork unitOfWork;
    private final RepresentativeRepository representativeRepository;

    public RepresentativeService(
            UnitOfWork unitOfWork,
            RepresentativeRepository representativeRepository
    ) {
        this.unitOfWork = unitOfWork;
        this.representativeRepository = representativeRepository;
    }

    public Representative createRepresentative(CreateRepresentativeDTO dto) {
        logger.info("RepresentativeService.createRepresentative - Starting");

        if (representativeRepository.findByCpf(dto.cpf()).isPresent()) {
            throw new RepresentativeAlreadyExistsException();
        }

        unitOfWork.begin();
        Representative representative = representativeRepository.save(new Representative(
            UUID.randomUUID().toString(),
            dto.cpf(),
            dto.name()
        ));
        unitOfWork.commit();

        logger.info("RepresentativeService.createRepresentative - Successfully finished");
        return representative;
    }

    public Representative getRepresentativeByCpf(String cpf) {
        return representativeRepository.findByCpf(cpf)
            .orElseThrow(RepresentativeNotFoundException::new);
    }

    public List<Representative> listRepresentatives(String name, int page, int pageSize) {
        return representativeRepository.findAll(name, page, pageSize);
    }
}
