package com.part.part_service.infrastructure.jpa.specifications;

import com.part.part_service.infrastructure.jpa.entities.PartEntity;
import org.springframework.data.jpa.domain.Specification;

public class PartSpecification {

    public static Specification<PartEntity> withFilters(String name) {
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
