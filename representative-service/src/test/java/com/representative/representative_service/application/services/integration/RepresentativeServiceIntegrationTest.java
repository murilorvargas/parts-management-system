package com.representative.representative_service.application.services.integration;

import com.representative.representative_service.application.dtos.input.CreateRepresentativeDTO;
import com.representative.representative_service.application.services.RepresentativeService;
import com.representative.representative_service.domain.exceptions.RepresentativeAlreadyExistsException;
import com.representative.representative_service.domain.exceptions.RepresentativeNotFoundException;
import com.representative.representative_service.infrastructure.jpa.repositories.RepresentativeRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class RepresentativeServiceIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("representative")
        .withUsername("test")
        .withPassword("test")
        .withInitScript("database-test-init.sql");

    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private RepresentativeService representativeService;

    @Autowired
    private RepresentativeRepositoryImpl representativeRepositoryImpl;

    @BeforeEach
    void setUpRequestScope() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void tearDownRequestScope() {
        RequestContextHolder.resetRequestAttributes();
    }

    private CreateRepresentativeDTO buildDto(String cpf, String name) {
        return new CreateRepresentativeDTO(cpf, name);
    }

    @Test
    void createRepresentative_shouldPersistToRealDatabase() {
        var created = representativeService.createRepresentative(buildDto("11122233344", "Alice"));

        var found = representativeService.getRepresentativeByCpf("11122233344");

        assertThat(found.getCpf()).isEqualTo("11122233344");
        assertThat(found.getName()).isEqualTo("Alice");
        assertThat(found.getPublicKey()).isEqualTo(created.getPublicKey());
    }

    @Test
    void createRepresentative_shouldThrowAlreadyExists_whenCpfIsDuplicated() {
        representativeService.createRepresentative(buildDto("22233344455", "Bob"));

        assertThatThrownBy(() -> representativeService.createRepresentative(buildDto("22233344455", "Bob Duplicated")))
            .isInstanceOf(RepresentativeAlreadyExistsException.class);
    }

    @Test
    void getRepresentativeByCpf_shouldThrowNotFound_whenCpfDoesNotExist() {
        assertThatThrownBy(() -> representativeService.getRepresentativeByCpf("00000000000"))
            .isInstanceOf(RepresentativeNotFoundException.class);
    }

    @Test
    void listRepresentatives_shouldFilterByNameAndPaginate() {
        representativeService.createRepresentative(buildDto("33344455566", "Carlos Silva"));
        representativeService.createRepresentative(buildDto("44455566677", "Carla Souza"));
        representativeService.createRepresentative(buildDto("55566677788", "Daniel Reis"));

        var filtered = representativeService.listRepresentatives("Car", 1, 30);

        assertThat(filtered).hasSize(2);
        assertThat(filtered).extracting("name")
            .containsExactlyInAnyOrder("Carlos Silva", "Carla Souza");

        var page1 = representativeService.listRepresentatives(null, 1, 2);
        assertThat(page1).hasSize(2);
    }

    @Test
    void repository_shouldBypassServiceButStillBeBlockedByUniqueConstraint() {
        representativeService.createRepresentative(buildDto("66677788899", "Eve"));

        var duplicate = new com.representative.representative_service.domain.entities.Representative(
            java.util.UUID.randomUUID().toString(),
            "66677788899",
            "Eve Duplicated"
        );

        assertThatThrownBy(() -> representativeRepositoryImpl.save(duplicate))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
