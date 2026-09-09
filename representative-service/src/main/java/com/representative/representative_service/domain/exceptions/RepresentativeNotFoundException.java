package com.representative.representative_service.domain.exceptions;

public class RepresentativeNotFoundException extends DomainException {

    public RepresentativeNotFoundException() {
        super("Representative not found");
    }

    @Override
    public String getCode() {
        return "REP00001";
    }

    @Override
    public String getMessageKey() {
        return "error.representative.not_found";
    }
}
