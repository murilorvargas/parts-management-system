package com.part.part_service.infrastructure.jpa.repositories;

import com.part.part_service.domain.entities.Part;
import com.part.part_service.infrastructure.jpa.entities.PartEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
class PartRepositoryImplTest {

    @Mock
    private JpaPartRepository jpaRepository;

    @InjectMocks
    private PartRepositoryImpl repository;

    @Captor
    private ArgumentCaptor<PartEntity> entityCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    private PartEntity sampleEntity() {
        return new PartEntity(
            1L,
            "11111111-1111-1111-1111-111111111111",
            "IDN-001",
            "Filtro de oleo",
            "Filtro de oleo original",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void save_shouldMapDomainToEntityAndBackToDomain() {
        var domainToSave = new Part(
            "11111111-1111-1111-1111-111111111111",
            "IDN-001",
            "Filtro de oleo",
            "Filtro de oleo original"
        );
        var savedEntity = sampleEntity();
        when(jpaRepository.save(any(PartEntity.class))).thenReturn(savedEntity);

        var result = repository.save(domainToSave);

        verify(jpaRepository).save(entityCaptor.capture());
        var capturedEntity = entityCaptor.getValue();
        assertThat(capturedEntity.getIdentificationNumber()).isEqualTo("IDN-001");
        assertThat(capturedEntity.getName()).isEqualTo("Filtro de oleo");
        assertThat(capturedEntity.getDescription()).isEqualTo("Filtro de oleo original");
        assertThat(capturedEntity.getPublicKey()).isEqualTo("11111111-1111-1111-1111-111111111111");

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getIdentificationNumber()).isEqualTo("IDN-001");
        assertThat(result.getName()).isEqualTo("Filtro de oleo");
        assertThat(result.getDescription()).isEqualTo("Filtro de oleo original");
    }

    @Test
    void findByIdentificationNumber_shouldReturnDomain_whenFound() {
        when(jpaRepository.findByIdentificationNumber("IDN-001")).thenReturn(Optional.of(sampleEntity()));

        var result = repository.findByIdentificationNumber("IDN-001");

        assertThat(result).isPresent();
        assertThat(result.get().getIdentificationNumber()).isEqualTo("IDN-001");
    }

    @Test
    void findByIdentificationNumber_shouldReturnEmpty_whenNotFound() {
        when(jpaRepository.findByIdentificationNumber("UNKNOWN")).thenReturn(Optional.empty());

        var result = repository.findByIdentificationNumber("UNKNOWN");

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldConvertOneBasedPageToZeroBasedPageable() {
        var entity = sampleEntity();
        when(jpaRepository.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(entity)));

        var result = repository.findAll("Filtro", 2, 10);

        verify(jpaRepository).findAll(any(Specification.class), pageableCaptor.capture());
        var capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable).isEqualTo(PageRequest.of(1, 10));
        assertThat(capturedPageable.getPageNumber()).isEqualTo(1);
        assertThat(capturedPageable.getPageSize()).isEqualTo(10);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdentificationNumber()).isEqualTo("IDN-001");
    }
}
