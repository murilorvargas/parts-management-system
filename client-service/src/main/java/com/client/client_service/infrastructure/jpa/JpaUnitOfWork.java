package com.client.client_service.infrastructure.jpa;

import com.client.client_service.domain.UnitOfWork;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
@RequestScope
public class JpaUnitOfWork implements UnitOfWork {

    private final PlatformTransactionManager transactionManager;
    private TransactionStatus transactionStatus;

    public JpaUnitOfWork(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void begin() {
        if (transactionStatus != null && !transactionStatus.isCompleted()) {
            return;
        }
        var definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        this.transactionStatus = transactionManager.getTransaction(definition);
    }

    @Override
    public void commit() {
        if (transactionStatus == null) {
            throw new IllegalStateException("No active transaction to commit");
        }
        transactionManager.commit(transactionStatus);
        transactionStatus = null;
    }

    @Override
    public void rollback() {
        if (transactionStatus == null) {
            throw new IllegalStateException("No active transaction to rollback");
        }
        transactionManager.rollback(transactionStatus);
        transactionStatus = null;
    }

    @PreDestroy
    public void cleanup() {
        if (transactionStatus != null && !transactionStatus.isCompleted()) {
            transactionManager.rollback(transactionStatus);
            transactionStatus = null;
        }
    }
}
