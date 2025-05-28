package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.TechnicianShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface TechnicianShiftRepository extends JpaRepository<TechnicianShift, Long> {
    List<TechnicianShift> findByWeekday(DayOfWeek weekday);
}
