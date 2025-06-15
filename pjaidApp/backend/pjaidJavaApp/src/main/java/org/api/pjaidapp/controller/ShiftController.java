package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.ShiftDto;
import org.api.pjaidapp.service.ShiftCalendarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private static final Logger logger = LoggerFactory.getLogger(ShiftController.class);
    private final ShiftCalendarService shiftCalendarService;

    // Pobierz zmiany dla konkretnej daty
    @GetMapping("/date/{date}")
    public ResponseEntity<?> getShift(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ShiftDto> shifts = shiftCalendarService.getShiftsByDate(date);
        return ResponseEntity.ok(shifts);
    }

    // Tymczasowe ładowanie z Excela - testowo
    @PostMapping("/import")
    public ResponseEntity<String> importExcel() {
        shiftCalendarService.loadFromExcel();
        return ResponseEntity.ok("Dane zaimportowane z Excela");
    }

    // Harmonogram
    @GetMapping("/harmonogram")
    public ResponseEntity<List<ShiftDto>> getHarmonogram() {
        logger.info("Wywołano endpoint harmonogram");
        List<ShiftDto> result = shiftCalendarService.getCurrentShifts();
        return ResponseEntity.ok(result);
    }

    // Technicy pracujący w danym dniu
    @GetMapping("/technicians/date/{date}")
    public ResponseEntity<List<ShiftDto>> getTechniciansByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(shiftCalendarService.getShiftsByDate(date));
    }

    // Technicy pracujący teraz
    @GetMapping("/technicians/active")
    public ResponseEntity<List<ShiftDto>> getActiveShiftsNow() {
        return ResponseEntity.ok(shiftCalendarService.getActiveShiftsNow());
    }
}
