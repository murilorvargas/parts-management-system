package com.representative.representative_service.domain.exceptions;

public class RepresentativeAlreadyExistsException extends DomainException {

    public RepresentativeAlreadyExistsException() {
        super("Representative already exists");
    }

    @Override
    public String getCode() {
        return "REP00002";
    }

    @Override
    public String getMessageKey() {
        return "error.representative.already_exists";
    }
}
