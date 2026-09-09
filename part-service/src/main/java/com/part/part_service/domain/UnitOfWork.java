package com.part.part_service.domain;

public interface UnitOfWork {

    void begin();

    void commit();

    void rollback();
}
