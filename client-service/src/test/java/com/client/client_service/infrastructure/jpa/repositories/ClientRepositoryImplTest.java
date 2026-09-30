package com.client.client_service.infrastructure.jpa.repositories;

import com.client.client_service.domain.entities.Client;
import com.client.client_service.infrastructure.jpa.entities.ClientEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientRepositoryImplTest {

    @Mock
    private JpaClientRepository jpaRepository;

    @InjectMocks
    private ClientRepositoryImpl clientRepositoryImpl;

    private ClientEntity buildEntity() {
        return new ClientEntity(
            1L,
            "11111111-1111-1111-1111-111111111111",
            "12345678901",
            "John Doe",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void save_shouldMapDomainToEntityAndBack() {
        var domain = new Client("11111111-1111-1111-1111-111111111111", "12345678901", "John Doe");
        var savedEntity = buildEntity();

        when(jpaRepository.save(any(ClientEntity.class))).thenReturn(savedEntity);

        var result = clientRepositoryImpl.save(domain);

        var captor = ArgumentCaptor.forClass(ClientEntity.class);
        verify(jpaRepository).save(captor.capture());
        var entityPassed = captor.getValue();

        assertThat(entityPassed.getCpf()).isEqualTo(domain.getCpf());
        assertThat(entityPassed.getName()).isEqualTo(domain.getName());
        assertThat(entityPassed.getPublicKey()).isEqualTo(domain.getPublicKey());

        assertThat(result.getId()).isEqualTo(savedEntity.getId());
        assertThat(result.getCpf()).isEqualTo(savedEntity.getCpf());
        assertThat(result.getName()).isEqualTo(savedEntity.getName());
        assertThat(result.getPublicKey()).isEqualTo(savedEntity.getPublicKey());
    }

    @Test
    void findByCpf_shouldReturnClient_whenFound() {
        var entity = buildEntity();
        when(jpaRepository.findByCpf("12345678901")).thenReturn(Optional.of(entity));

        var result = clientRepositoryImpl.findByCpf("12345678901");

        assertThat(result).isPresent();
        assertThat(result.get().getCpf()).isEqualTo(entity.getCpf());
        assertThat(result.get().getName()).isEqualTo(entity.getName());
    }

    @Test
    void findByCpf_shouldReturnEmpty_whenNotFound() {
        when(jpaRepository.findByCpf("00000000000")).thenReturn(Optional.empty());

        var result = clientRepositoryImpl.findByCpf("00000000000");

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldConvertOneBasedPageToZeroBasedPageable() {
        var entity = buildEntity();
        var page = new PageImpl<>(List.of(entity));

        when(jpaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        var result = clientRepositoryImpl.findAll("John", 2, 10);

        var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(jpaRepository).findAll(any(Specification.class), pageableCaptor.capture());

        var capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable.getPageNumber()).isEqualTo(1);
        assertThat(capturedPageable.getPageSize()).isEqualTo(10);
        assertThat(capturedPageable).isEqualTo(PageRequest.of(1, 10));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCpf()).isEqualTo(entity.getCpf());
    }
}
