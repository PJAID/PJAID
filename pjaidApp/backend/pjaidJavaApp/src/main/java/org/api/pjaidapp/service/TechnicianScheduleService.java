package org.api.pjaidapp.service;

import lombok.RequiredArgsConstructor;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianScheduleService {

    private final ShiftCalendarService shiftCalendarService;
    private final UserRepository userRepository;

    public List<User> getTechniciansOnDuty(LocalDate date) {
        String shift = shiftCalendarService.getShiftForDate(date);
        return userRepository.findByZmiana(shift);
    }
}
