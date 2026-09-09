package com.client.client_service.infrastructure.jpa.specifications;

import com.client.client_service.infrastructure.jpa.entities.ClientEntity;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {

    public static Specification<ClientEntity> withFilters(String name) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (name != null && !name.isBlank()) {
                predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            return predicate;
        };
    }
}
