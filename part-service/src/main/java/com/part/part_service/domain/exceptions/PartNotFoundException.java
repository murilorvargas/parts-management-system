package com.part.part_service.domain.exceptions;

public class PartNotFoundException extends DomainException {

    public PartNotFoundException() {
        super("Part not found");
    }

    @Override
    public String getCode() {
        return "PRT00001";
    }

    @Override
    public String getMessageKey() {
        return "error.part.not_found";
    }
}
