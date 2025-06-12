package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findAllByDate(LocalDate date);
}

