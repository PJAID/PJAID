package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.TechnicianLoginStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TechnicianLoginStatusRepository extends JpaRepository<TechnicianLoginStatus, Long> {
    @Query("SELECT tls.technicianId FROM TechnicianLoginStatus tls WHERE tls.isLoggedIn = true")
    List<Long> findCurrentlyLoggedInTechnicianIds();
}
