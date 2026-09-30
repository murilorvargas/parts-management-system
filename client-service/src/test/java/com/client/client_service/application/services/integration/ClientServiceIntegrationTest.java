package com.client.client_service.application.services.integration;

import com.client.client_service.application.dtos.input.CreateClientDTO;
import com.client.client_service.application.services.ClientService;
import com.client.client_service.domain.exceptions.ClientAlreadyExistsException;
import com.client.client_service.domain.exceptions.ClientNotFoundException;
import com.client.client_service.infrastructure.jpa.repositories.ClientRepositoryImpl;
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
class ClientServiceIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("client")
        .withUsername("client_user")
        .withPassword("client_password")
        .withInitScript("database-test-init.sql");

    @DynamicPropertySource
    static void registerDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepositoryImpl clientRepositoryImpl;

    @BeforeEach
    void setUpRequestScope() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void tearDownRequestScope() {
        RequestContextHolder.resetRequestAttributes();
    }

    private String randomCpf() {
        return String.valueOf(10000000000L + Math.abs(UUID.randomUUID().getMostSignificantBits() % 89999999999L));
    }

    @Test
    void createClient_shouldPersistClient_andBeRetrievableByCpf() {
        var cpf = randomCpf();
        var dto = new CreateClientDTO(cpf, "John Doe");

        var created = clientService.createClient(dto);

        assertThat(created.getCpf()).isEqualTo(cpf);
        assertThat(created.getName()).isEqualTo("John Doe");
        assertThat(created.getPublicKey()).isNotBlank();

        var found = clientService.getClientByCpf(cpf);
        assertThat(found.getCpf()).isEqualTo(cpf);
        assertThat(found.getName()).isEqualTo("John Doe");
    }

    @Test
    void createClient_shouldThrowClientAlreadyExistsException_whenCpfIsDuplicated() {
        var cpf = randomCpf();
        clientService.createClient(new CreateClientDTO(cpf, "John Doe"));

        assertThatThrownBy(() -> clientService.createClient(new CreateClientDTO(cpf, "Another Name")))
            .isInstanceOf(ClientAlreadyExistsException.class);
    }

    @Test
    void getClientByCpf_shouldThrowClientNotFoundException_whenCpfDoesNotExist() {
        assertThatThrownBy(() -> clientService.getClientByCpf("00000000000"))
            .isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    void listClients_shouldFilterByNameAndPaginate_whenMultipleClientsExist() {
        clientService.createClient(new CreateClientDTO(randomCpf(), "Alice Wonderland"));
        clientService.createClient(new CreateClientDTO(randomCpf(), "Alice Springs"));
        clientService.createClient(new CreateClientDTO(randomCpf(), "Bob Builder"));

        var aliceResults = clientService.listClients("Alice", 1, 30);
        assertThat(aliceResults).hasSize(2);
        assertThat(aliceResults).allSatisfy(client -> assertThat(client.getName()).contains("Alice"));

        var firstPage = clientService.listClients(null, 1, 2);
        assertThat(firstPage).hasSize(2);
    }

    @Test
    void save_shouldThrowDataIntegrityViolationException_whenCpfAlreadyExistsAndServiceIsBypassed() {
        var cpf = randomCpf();
        clientService.createClient(new CreateClientDTO(cpf, "John Doe"));

        var duplicate = new com.client.client_service.domain.entities.Client(
            UUID.randomUUID().toString(),
            cpf,
            "Duplicate Name"
        );

        assertThatThrownBy(() -> clientRepositoryImpl.save(duplicate))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
