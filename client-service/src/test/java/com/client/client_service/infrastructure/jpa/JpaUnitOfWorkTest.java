package com.client.client_service.infrastructure.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaUnitOfWorkTest {

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private TransactionStatus transactionStatus;

    private JpaUnitOfWork unitOfWork;

    @BeforeEach
    void setUp() {
        unitOfWork = new JpaUnitOfWork(transactionManager);
    }

    @Test
    void begin_shouldStartNewTransaction_whenThereIsNoActiveTransaction() {
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);

        unitOfWork.begin();

        verify(transactionManager, times(1)).getTransaction(any(TransactionDefinition.class));
    }

    @Test
    void begin_shouldBeNoOp_whenThereIsAlreadyAnActiveNonCompletedTransaction() {
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        when(transactionStatus.isCompleted()).thenReturn(false);

        unitOfWork.begin();
        unitOfWork.begin();

        verify(transactionManager, times(1)).getTransaction(any(TransactionDefinition.class));
    }

    @Test
    void commit_shouldThrowIllegalStateException_whenThereIsNoActiveTransaction() {
        assertThatThrownBy(() -> unitOfWork.commit())
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("No active transaction to commit");
    }

    @Test
    void rollback_shouldThrowIllegalStateException_whenThereIsNoActiveTransaction() {
        assertThatThrownBy(() -> unitOfWork.rollback())
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("No active transaction to rollback");
    }

    @Test
    void commit_shouldCommitActiveTransaction() {
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);

        unitOfWork.begin();
        unitOfWork.commit();

        verify(transactionManager, times(1)).commit(transactionStatus);
    }

    @Test
    void rollback_shouldRollbackActiveTransaction() {
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);

        unitOfWork.begin();
        unitOfWork.rollback();

        verify(transactionManager, times(1)).rollback(transactionStatus);
    }

    @Test
    void cleanup_shouldRollbackAsSafetyNet_whenTransactionWasNotCompleted() {
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        when(transactionStatus.isCompleted()).thenReturn(false);

        unitOfWork.begin();
        unitOfWork.cleanup();

        verify(transactionManager, times(1)).rollback(transactionStatus);
    }

    @Test
    void cleanup_shouldNotRollback_whenTransactionWasAlreadyCompleted() {
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        when(transactionStatus.isCompleted()).thenReturn(true);

        unitOfWork.begin();
        unitOfWork.cleanup();

        verify(transactionManager, times(0)).rollback(any(TransactionStatus.class));
    }

    @Test
    void cleanup_shouldBeNoOp_whenThereIsNoTransactionAtAll() {
        unitOfWork.cleanup();

        verify(transactionManager, times(0)).rollback(any(TransactionStatus.class));
    }
}
