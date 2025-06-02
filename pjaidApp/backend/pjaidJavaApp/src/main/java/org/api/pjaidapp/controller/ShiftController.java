package org.api.pjaidapp.controller;

import org.api.pjaidapp.service.ShiftCalendarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/shift")
public class ShiftController {

    private final ShiftCalendarService shiftCalendarService;

    public ShiftController(ShiftCalendarService shiftCalendarService) {
        this.shiftCalendarService = shiftCalendarService;
    }

    @GetMapping
    public String getShift(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return shiftCalendarService.getShiftForDate(date);
    }
}

