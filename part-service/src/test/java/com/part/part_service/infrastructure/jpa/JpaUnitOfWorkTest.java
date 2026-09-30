package com.part.part_service.infrastructure.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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

    private JpaUnitOfWork newUnitOfWork() {
        return new JpaUnitOfWork(transactionManager);
    }

    @Test
    void begin_shouldStartNewTransaction_whenNoneActive() {
        unitOfWork = newUnitOfWork();
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);

        unitOfWork.begin();

        verify(transactionManager, times(1)).getTransaction(any(TransactionDefinition.class));
    }

    @Test
    void begin_shouldBeNoOp_whenAlreadyActiveAndNotCompleted() {
        unitOfWork = newUnitOfWork();
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        when(transactionStatus.isCompleted()).thenReturn(false);

        unitOfWork.begin();
        unitOfWork.begin();

        verify(transactionManager, times(1)).getTransaction(any(TransactionDefinition.class));
    }

    @Test
    void commit_shouldThrow_whenNoActiveTransaction() {
        unitOfWork = newUnitOfWork();

        assertThatThrownBy(() -> unitOfWork.commit())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("No active transaction to commit");
    }

    @Test
    void rollback_shouldThrow_whenNoActiveTransaction() {
        unitOfWork = newUnitOfWork();

        assertThatThrownBy(() -> unitOfWork.rollback())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("No active transaction to rollback");
    }

    @Test
    void commit_shouldDelegateToTransactionManager_andClearTransaction() {
        unitOfWork = newUnitOfWork();
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        unitOfWork.begin();

        unitOfWork.commit();

        verify(transactionManager).commit(transactionStatus);
        assertThatThrownBy(() -> unitOfWork.commit())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void rollback_shouldDelegateToTransactionManager_andClearTransaction() {
        unitOfWork = newUnitOfWork();
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        unitOfWork.begin();

        unitOfWork.rollback();

        verify(transactionManager).rollback(transactionStatus);
        assertThatThrownBy(() -> unitOfWork.rollback())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void cleanup_shouldSafetyRollback_whenTransactionNotCompleted() {
        unitOfWork = newUnitOfWork();
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        when(transactionStatus.isCompleted()).thenReturn(false);
        unitOfWork.begin();

        unitOfWork.cleanup();

        verify(transactionManager).rollback(transactionStatus);
    }

    @Test
    void cleanup_shouldDoNothing_whenNoActiveTransaction() {
        unitOfWork = newUnitOfWork();

        unitOfWork.cleanup();

        verify(transactionManager, never()).rollback(any(TransactionStatus.class));
    }

    @Test
    void cleanup_shouldDoNothing_whenTransactionAlreadyCompleted() {
        unitOfWork = newUnitOfWork();
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        when(transactionStatus.isCompleted()).thenReturn(true);
        unitOfWork.begin();

        unitOfWork.cleanup();

        verify(transactionManager, never()).rollback(any(TransactionStatus.class));
    }
}
