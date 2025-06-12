package org.api.pjaidapp.controller;

import lombok.RequiredArgsConstructor;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.service.TechnicianScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianScheduleService scheduleService;

    @GetMapping("/{date}")
    public ResponseEntity<List<User>> getTechniciansByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getTechniciansOnDuty(date));
    }

    @GetMapping("/today")
    public ResponseEntity<List<User>> getTodayTechnicians() {
        return ResponseEntity.ok(scheduleService.getTechniciansOnDuty(LocalDate.now()));
    }
}
