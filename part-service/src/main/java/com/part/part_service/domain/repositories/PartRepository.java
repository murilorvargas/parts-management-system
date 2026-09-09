package com.part.part_service.domain.repositories;

import com.part.part_service.domain.entities.Part;

import java.util.List;
import java.util.Optional;

public interface PartRepository {

    Part save(Part part);

    Optional<Part> findByIdentificationNumber(String identificationNumber);

    List<Part> findAll(String name, int page, int pageSize);
}
