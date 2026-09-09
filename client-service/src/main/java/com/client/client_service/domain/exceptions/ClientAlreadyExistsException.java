package com.client.client_service.domain.exceptions;

public class ClientAlreadyExistsException extends DomainException {

    public ClientAlreadyExistsException() {
        super("Client already exists");
    }

    @Override
    public String getCode() {
        return "CLI00002";
    }

    @Override
    public String getMessageKey() {
        return "error.client.already_exists";
    }
}
