package com.client.client_service.domain.exceptions;

public class ClientNotFoundException extends DomainException {

    public ClientNotFoundException() {
        super("Client not found");
    }

    @Override
    public String getCode() {
        return "CLI00001";
    }

    @Override
    public String getMessageKey() {
        return "error.client.not_found";
    }
}
