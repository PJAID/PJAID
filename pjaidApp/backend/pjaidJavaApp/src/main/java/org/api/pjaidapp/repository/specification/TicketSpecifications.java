package org.api.pjaidapp.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.model.Device;
import org.api.pjaidapp.model.Ticket;
import org.api.pjaidapp.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TicketSpecifications {

    public static Specification<Ticket> withFilters(Status status, String userName, String device, String titleContains) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (titleContains != null && !titleContains.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + titleContains.toLowerCase() + "%"));
            }

            if (userName != null && !userName.isEmpty()) {
                Join<Ticket, User> userJoin = root.join("user");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("userName")), "%" + userName.toLowerCase() + "%"));
            }

            if (device != null && !device.isEmpty()) {
                Join<Ticket, Device> deviceJoin = root.join("device");
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(deviceJoin.get("deviceName")), "%" + device.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}