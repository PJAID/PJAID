package org.api.pjaidapp.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.api.pjaidapp.model.Device;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DeviceSpecifications {

    private DeviceSpecifications() {
    }

    public static Specification<Device> withFilters(String name, String purchaseDate, String lastService) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(name)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (StringUtils.hasText(purchaseDate)) {
                predicates.add(criteriaBuilder.like(root.get("purchaseDate"), "%" + purchaseDate + "%"));
            }

            if (StringUtils.hasText(lastService)) {
                predicates.add(criteriaBuilder.like(root.get("lastService"), "%" + lastService + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
