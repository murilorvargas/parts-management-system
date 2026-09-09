package com.part.part_service.domain.exceptions;

public class PartAlreadyExistsException extends DomainException {

    public PartAlreadyExistsException() {
        super("Part already exists");
    }

    @Override
    public String getCode() {
        return "PRT00002";
    }

    @Override
    public String getMessageKey() {
        return "error.part.already_exists";
    }
}
