package org.api.pjaidapp.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShiftCalendarService {

    private final Map<LocalDate, String> schedule;

    public ShiftCalendarService() {
        this.schedule = new HashMap<>();
        loadSampleSchedule();
    }

    private void loadSampleSchedule() {
        schedule.put(LocalDate.of(2025, 5, 12), "A");
        schedule.put(LocalDate.of(2025, 5, 13), "B");
        schedule.put(LocalDate.of(2025, 5, 14), "C");
        schedule.put(LocalDate.of(2025, 5, 15), "D");
        schedule.put(LocalDate.of(2025, 5, 16), "A");

    }

    public String getShiftForDate(LocalDate date) {
        return schedule.getOrDefault(date, "A");
    }
}

