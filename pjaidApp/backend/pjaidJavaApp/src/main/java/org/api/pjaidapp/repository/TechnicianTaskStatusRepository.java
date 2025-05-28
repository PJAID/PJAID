package org.api.pjaidapp.repository;

import org.api.pjaidapp.model.TechnicianTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TechnicianTaskStatusRepository extends JpaRepository<TechnicianTaskStatus, Long> {
    List<TechnicianTaskStatus> findByTaskStatus(String taskStatus);

    @Query("SELECT tts.technician.id FROM TechnicianTaskStatus tts WHERE tts.taskStatus = 'available'")
    List<Long> findAvailableTechnicianIds();
}
