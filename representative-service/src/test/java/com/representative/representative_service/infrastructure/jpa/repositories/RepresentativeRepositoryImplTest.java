package com.representative.representative_service.infrastructure.jpa.repositories;

import com.representative.representative_service.domain.entities.Representative;
import com.representative.representative_service.infrastructure.jpa.entities.RepresentativeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepresentativeRepositoryImplTest {

    @Mock
    private JpaRepresentativeRepository jpaRepository;

    @InjectMocks
    private RepresentativeRepositoryImpl repositoryImpl;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private RepresentativeEntity buildEntity(String cpf, String name) {
        return new RepresentativeEntity(
            1L,
            UUID.randomUUID().toString(),
            cpf,
            name,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void save_shouldMapDomainToEntityAndBack() {
        var domain = new Representative(UUID.randomUUID().toString(), "12345678901", "John Doe");
        var savedEntity = buildEntity("12345678901", "John Doe");

        when(jpaRepository.save(any(RepresentativeEntity.class))).thenReturn(savedEntity);

        var result = repositoryImpl.save(domain);

        assertThat(result.getCpf()).isEqualTo("12345678901");
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findByCpf_shouldReturnRepresentative_whenFound() {
        var entity = buildEntity("12345678901", "John Doe");
        when(jpaRepository.findByCpf("12345678901")).thenReturn(Optional.of(entity));

        var result = repositoryImpl.findByCpf("12345678901");

        assertThat(result).isPresent();
        assertThat(result.get().getCpf()).isEqualTo("12345678901");
    }

    @Test
    void findByCpf_shouldReturnEmpty_whenNotFound() {
        when(jpaRepository.findByCpf("00000000000")).thenReturn(Optional.empty());

        var result = repositoryImpl.findByCpf("00000000000");

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldConvertOneBasedPageToZeroBasedPageable() {
        var entity = buildEntity("12345678901", "John Doe");
        Page<RepresentativeEntity> page = new PageImpl<>(List.of(entity));

        when(jpaRepository.findAll(any(Specification.class), pageableCaptor.capture())).thenReturn(page);

        var result = repositoryImpl.findAll("John", 1, 30);

        assertThat(result).hasSize(1);
        assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(30);
    }

    @Test
    void findAll_shouldConvertSecondPageCorrectly() {
        Page<RepresentativeEntity> page = new PageImpl<>(List.of());

        when(jpaRepository.findAll(any(Specification.class), pageableCaptor.capture())).thenReturn(page);

        repositoryImpl.findAll(null, 2, 10);

        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(1);
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(10);
    }

    @Test
    void findAll_shouldVerifyJpaRepositoryIsCalledOnce() {
        Page<RepresentativeEntity> page = new PageImpl<>(List.of());
        when(jpaRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        repositoryImpl.findAll("John", 1, 30);

        verify(jpaRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}
