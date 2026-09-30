package com.representative.representative_service.infrastructure.jpa;

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

    @Test
    void begin_shouldCreateNewTransaction_whenNoneIsActive() {
        var unitOfWork = new JpaUnitOfWork(transactionManager);
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);

        unitOfWork.begin();

        verify(transactionManager, times(1)).getTransaction(any(TransactionDefinition.class));
    }

    @Test
    void begin_shouldBeNoOp_whenActiveTransactionIsNotCompleted() {
        var unitOfWork = new JpaUnitOfWork(transactionManager);
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        when(transactionStatus.isCompleted()).thenReturn(false);

        unitOfWork.begin();
        unitOfWork.begin();

        verify(transactionManager, times(1)).getTransaction(any(TransactionDefinition.class));
    }

    @Test
    void commit_shouldThrowIllegalStateException_whenNoActiveTransaction() {
        var unitOfWork = new JpaUnitOfWork(transactionManager);

        assertThatThrownBy(unitOfWork::commit)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("No active transaction to commit");
    }

    @Test
    void rollback_shouldThrowIllegalStateException_whenNoActiveTransaction() {
        var unitOfWork = new JpaUnitOfWork(transactionManager);

        assertThatThrownBy(unitOfWork::rollback)
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("No active transaction to rollback");
    }

    @Test
    void commit_shouldCommitActiveTransaction() {
        var unitOfWork = new JpaUnitOfWork(transactionManager);
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);

        unitOfWork.begin();
        unitOfWork.commit();

        verify(transactionManager).commit(transactionStatus);
    }

    @Test
    void rollback_shouldRollbackActiveTransaction() {
        var unitOfWork = new JpaUnitOfWork(transactionManager);
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);

        unitOfWork.begin();
        unitOfWork.rollback();

        verify(transactionManager).rollback(transactionStatus);
    }

    @Test
    void cleanup_shouldRollbackAsSafetyNet_whenTransactionWasNotCompleted() throws Exception {
        var unitOfWork = new JpaUnitOfWork(transactionManager);
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);
        when(transactionStatus.isCompleted()).thenReturn(false);

        unitOfWork.begin();
        var cleanupMethod = JpaUnitOfWork.class.getDeclaredMethod("cleanup");
        cleanupMethod.setAccessible(true);
        cleanupMethod.invoke(unitOfWork);

        verify(transactionManager).rollback(transactionStatus);
    }

    @Test
    void cleanup_shouldDoNothing_whenTransactionWasAlreadyCompleted() throws Exception {
        var unitOfWork = new JpaUnitOfWork(transactionManager);
        when(transactionManager.getTransaction(any(TransactionDefinition.class))).thenReturn(transactionStatus);

        unitOfWork.begin();
        unitOfWork.commit();

        var cleanupMethod = JpaUnitOfWork.class.getDeclaredMethod("cleanup");
        cleanupMethod.setAccessible(true);
        cleanupMethod.invoke(unitOfWork);

        verify(transactionManager, times(1)).commit(transactionStatus);
        verify(transactionManager, times(0)).rollback(any());
    }
}
