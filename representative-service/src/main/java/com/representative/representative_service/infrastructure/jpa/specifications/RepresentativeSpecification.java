package com.representative.representative_service.infrastructure.jpa.specifications;

import com.representative.representative_service.infrastructure.jpa.entities.RepresentativeEntity;
import org.springframework.data.jpa.domain.Specification;

public class RepresentativeSpecification {

    public static Specification<RepresentativeEntity> withFilters(String name) {
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
