package com.part.part_service.application.services.integration;

import com.part.part_service.application.dtos.input.CreatePartDTO;
import com.part.part_service.application.services.PartService;
import com.part.part_service.domain.entities.Part;
import com.part.part_service.domain.exceptions.PartAlreadyExistsException;
import com.part.part_service.domain.exceptions.PartNotFoundException;
import com.part.part_service.infrastructure.jpa.repositories.PartRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class PartServiceIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("part")
        .withUsername("test")
        .withPassword("test")
        .withInitScript("database-test-init.sql");

    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        // Mesma naming strategy usada em producao (config-repo/part-service.properties),
        // necessaria pois o schema mapeia a tabela como "Part" (case-sensitive no MySQL/Linux).
        registry.add("spring.jpa.hibernate.naming.physical-strategy",
            () -> "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
    }

    @Autowired
    private PartService partService;

    @Autowired
    private PartRepositoryImpl partRepositoryImpl;

    @BeforeEach
    void setUpRequestScope() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void tearDownRequestScope() {
        RequestContextHolder.resetRequestAttributes();
    }

    private CreatePartDTO newDto(String identificationNumber, String name) {
        return new CreatePartDTO(identificationNumber, name, "Descricao de " + name);
    }

    @Test
    void createPart_shouldPersistAndBeRetrievableByIdentificationNumber() {
        var identificationNumber = "IDN-" + UUID.randomUUID();
        var dto = newDto(identificationNumber, "Filtro de oleo");

        var created = partService.createPart(dto);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getPublicKey()).isNotNull();

        var found = partService.getPartByIdentificationNumber(identificationNumber);
        assertThat(found.getIdentificationNumber()).isEqualTo(identificationNumber);
        assertThat(found.getName()).isEqualTo("Filtro de oleo");
    }

    @Test
    void createPart_shouldThrowPartAlreadyExists_whenIdentificationNumberIsDuplicated() {
        var identificationNumber = "IDN-" + UUID.randomUUID();
        partService.createPart(newDto(identificationNumber, "Filtro de oleo"));

        assertThatThrownBy(() -> partService.createPart(newDto(identificationNumber, "Outro nome")))
            .isInstanceOf(PartAlreadyExistsException.class);
    }

    @Test
    void getPartByIdentificationNumber_shouldThrowPartNotFound_whenNotPersisted() {
        assertThatThrownBy(() -> partService.getPartByIdentificationNumber("DOES-NOT-EXIST-" + UUID.randomUUID()))
            .isInstanceOf(PartNotFoundException.class);
    }

    @Test
    void listParts_shouldFilterByNameAndPaginate() {
        var suffix = UUID.randomUUID();
        partService.createPart(newDto("IDN-A-" + suffix, "Filtro de ar " + suffix));
        partService.createPart(newDto("IDN-B-" + suffix, "Filtro de oleo " + suffix));
        partService.createPart(newDto("IDN-C-" + suffix, "Pastilha de freio " + suffix));

        var filtered = partService.listParts("Filtro", 1, 30);
        assertThat(filtered)
            .extracting(part -> part.getName())
            .anyMatch(name -> name.contains("Filtro de ar " + suffix))
            .anyMatch(name -> name.contains("Filtro de oleo " + suffix));
        assertThat(filtered)
            .noneMatch(part -> part.getName().contains("Pastilha de freio " + suffix));

        var firstPage = partService.listParts("Filtro " + "de", 1, 1);
        assertThat(firstPage).hasSize(1);
    }

    @Test
    void repository_shouldThrowDataIntegrityViolation_whenBypassingServiceWithDuplicateKey() {
        var identificationNumber = "IDN-" + UUID.randomUUID();
        var part = new Part(
            UUID.randomUUID().toString(),
            identificationNumber,
            "Filtro de oleo",
            "Descricao"
        );
        partRepositoryImpl.save(part);

        var duplicatedPublicKeyPart = new Part(
            UUID.randomUUID().toString(),
            identificationNumber,
            "Outro nome",
            "Outra descricao"
        );

        assertThatThrownBy(() -> partRepositoryImpl.save(duplicatedPublicKeyPart))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
