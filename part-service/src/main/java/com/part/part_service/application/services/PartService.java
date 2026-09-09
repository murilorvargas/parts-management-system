package com.part.part_service.application.services;

import com.part.part_service.application.dtos.input.CreatePartDTO;
import com.part.part_service.domain.UnitOfWork;
import com.part.part_service.domain.entities.Part;
import com.part.part_service.domain.exceptions.PartAlreadyExistsException;
import com.part.part_service.domain.exceptions.PartNotFoundException;
import com.part.part_service.domain.repositories.PartRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PartService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PartService.class);

    private final UnitOfWork unitOfWork;
    private final PartRepository partRepository;

    public PartService(
            UnitOfWork unitOfWork,
            PartRepository partRepository
    ) {
        this.unitOfWork = unitOfWork;
        this.partRepository = partRepository;
    }

    public Part createPart(CreatePartDTO dto) {
        logger.info("PartService.createPart - Starting");

        if (partRepository.findByIdentificationNumber(dto.identificationNumber()).isPresent()) {
            throw new PartAlreadyExistsException();
        }

        unitOfWork.begin();
        Part part = partRepository.save(new Part(
            UUID.randomUUID().toString(),
            dto.identificationNumber(),
            dto.name(),
            dto.description()
        ));
        unitOfWork.commit();

        logger.info("PartService.createPart - Successfully finished");
        return part;
    }

    public Part getPartByIdentificationNumber(String identificationNumber) {
        return partRepository.findByIdentificationNumber(identificationNumber)
            .orElseThrow(PartNotFoundException::new);
    }

    public List<Part> listParts(String name, int page, int pageSize) {
        return partRepository.findAll(name, page, pageSize);
    }
}
