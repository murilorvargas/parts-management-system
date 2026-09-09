package com.client.client_service.domain.exceptions;

public abstract class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public abstract String getCode();
    public abstract String getMessageKey();
}
