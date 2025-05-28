package org.api.pjaidapp.service;

import org.api.pjaidapp.enums.Priority;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.model.*;
import org.api.pjaidapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TicketAssignmentService {

    @Autowired private TicketQueueRepository ticketQueueRepo;
    @Autowired private TicketRepository ticketRepo;
    @Autowired private IncidentRepository incidentRepo;
    @Autowired private TechnicianShiftRepository shiftRepo;
    @Autowired private TechnicianLoginStatusRepository loginRepo;
    @Autowired private TechnicianTaskStatusRepository taskStatusRepo;
    @Autowired private TicketTimeRepository ticketTimeRepo;

    public void assignPendingTickets() {
        List<TicketQueue> waitingTickets = getSortedWaitingTickets();

        for (TicketQueue queueItem : waitingTickets) {
            Ticket ticket = queueItem.getTicket();
            List<User> availableTechnicians = getAvailableTechnicians(LocalDateTime.now());

            if (!availableTechnicians.isEmpty()) {
                assignToAvailableTechnician(ticket, queueItem, availableTechnicians);
            } else {
                reserveForNextTechnician(queueItem);
            }
        }
    }

    private List<TicketQueue> getSortedWaitingTickets() {
        return ticketQueueRepo.findByQueueStatus("waiting").stream()
                .sorted(Comparator
                        .comparing((TicketQueue tq) -> !tq.isUrgent())
                        .thenComparing(tq -> getIncidentPriority(tq.getTicket()))
                        .thenComparing(TicketQueue::getQueuedAt)
                )
                .collect(Collectors.toList());
    }

    private Priority getIncidentPriority(Ticket ticket) {
        return Optional.ofNullable(ticket.getIncident())
                .flatMap(incident -> incidentRepo.findById(incident.getId()))
                .map(Incident::getPriority)
                .orElse(Priority.LOW);
    }

    private List<User> getAvailableTechnicians(LocalDateTime now) {
        List<User> techniciansOnShift = getTechniciansOnShift(now);
        List<Long> loggedInIds = loginRepo.findCurrentlyLoggedInTechnicianIds();
        List<Long> availableIds = taskStatusRepo.findAvailableTechnicianIds();

        return techniciansOnShift.stream()
                .filter(tech -> loggedInIds.contains(tech.getId()))
                .filter(tech -> availableIds.contains(tech.getId()))
                .collect(Collectors.toList());
    }

    private List<User> getTechniciansOnShift(LocalDateTime now) {
        DayOfWeek day = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();

        return shiftRepo.findByWeekday(day).stream()
                .filter(shift -> !currentTime.isBefore(shift.getTimeStart()) && !currentTime.isAfter(shift.getTimeEnd()))
                .map(TechnicianShift::getTechnician)
                .collect(Collectors.toList());
    }

    private void assignToAvailableTechnician(Ticket ticket, TicketQueue queueItem, List<User> availableTechnicians) {
        User selectedTechnician = selectLeastLoadedTechnician(availableTechnicians);
        assignTicketToTechnician(ticket, selectedTechnician);

        queueItem.setQueueStatus("assigned");
        queueItem.setAssignedTechnicianId(selectedTechnician.getId());
        ticketQueueRepo.save(queueItem);
    }

    private void reserveForNextTechnician(TicketQueue queueItem) {
        User predictedTechnician = getNextAvailableTechnician();

        queueItem.setQueueStatus("waiting");
        queueItem.setAssignedTechnicianId(predictedTechnician.getId());
        ticketQueueRepo.save(queueItem);
    }

    private User selectLeastLoadedTechnician(List<User> technicians) {
        return technicians.stream()
                .min(Comparator.comparingLong(tech -> ticketRepo.countByTechnicianAndStatus(tech, Status.W_TRAKCIE)))
                .orElseThrow(() -> new IllegalStateException("No technicians available"));
    }

    private User getNextAvailableTechnician() {
        return taskStatusRepo.findByTaskStatus("busy").stream()
                .min(Comparator.comparing(this::getRemainingTaskTime))
                .map(TechnicianTaskStatus::getTechnician)
                .orElseThrow(() -> new IllegalStateException("No busy technicians found"));
    }

    private double getRemainingTaskTime(TechnicianTaskStatus taskStatus) {
        List<TicketTime> times = ticketTimeRepo.findByTechnicianId(taskStatus.getTechnician().getId());
        double averageDuration = times.stream()
                .mapToLong(tt -> Duration.between(tt.getTimeStart(), tt.getTimeEnd()).toMinutes())
                .average().orElse(30);
        long elapsed = Duration.between(taskStatus.getTimeStart(), LocalDateTime.now()).toMinutes();
        return averageDuration - elapsed;
    }

    private void assignTicketToTechnician(Ticket ticket, User technician) {
        ticket.setTechnician(technician);
        ticket.setStatus(Status.W_TRAKCIE);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepo.save(ticket);

        TechnicianTaskStatus status = new TechnicianTaskStatus();
        status.setTechnician(technician);
        status.setTaskStatus("busy");
        status.setTimeStart(LocalDateTime.now());
        taskStatusRepo.save(status);

        TicketTime time = new TicketTime();
        time.setTechnician(technician);
        time.setTicket(ticket);
        time.setTimeStart(LocalDateTime.now());
        ticketTimeRepo.save(time);
    }
}
