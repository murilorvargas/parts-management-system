package com.client.client_service.domain;

public interface UnitOfWork {

    void begin();

    void commit();

    void rollback();
}
