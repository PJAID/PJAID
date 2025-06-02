package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
}
