package com.representative.representative_service.domain;

public interface UnitOfWork {

    void begin();

    void commit();

    void rollback();
}
