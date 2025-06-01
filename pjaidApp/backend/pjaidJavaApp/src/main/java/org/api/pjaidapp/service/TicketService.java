package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.enums.Role;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.exception.DeviceNotFoundException;
import org.api.pjaidapp.exception.IncidentNotFoundException;
import org.api.pjaidapp.exception.TicketNotFoundException;
import org.api.pjaidapp.exception.UserNotFoundException;
import org.api.pjaidapp.mapper.TicketMapper;
import org.api.pjaidapp.model.Device;
import org.api.pjaidapp.model.Incident;
import org.api.pjaidapp.model.Ticket;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.repository.DeviceRepository;
import org.api.pjaidapp.repository.IncidentRepository;
import org.api.pjaidapp.repository.TicketRepository;
import org.api.pjaidapp.repository.UserRepository;
import org.api.pjaidapp.repository.specification.TicketSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final TicketMapper ticketMapper;

    private final IncidentRepository incidentRepository;

    private final ShiftCalendarService shiftCalendarService;


    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, DeviceRepository deviceRepository, TicketMapper ticketMapper, IncidentRepository incidentRepository, ShiftCalendarService shiftCalendarService) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.ticketMapper = ticketMapper;
        this.incidentRepository = incidentRepository;
        this.shiftCalendarService = shiftCalendarService;
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        return ticketMapper.toResponse(ticket);
    }

    public List<TicketResponse> getAllActiveTickets() {
        return ticketRepository.findByStatusIn(Arrays.asList(Status.NOWE, Status.W_TRAKCIE))
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    public TicketResponse createTicket(TicketRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId is required");
        }
        if (request.getDeviceId() == null) {
            throw new IllegalArgumentException("deviceId is required");
        }
        if (request.getIncidentId() == null) {
            throw new IllegalArgumentException("incidentId is required");
        }

        Ticket ticket = ticketMapper.toEntity(request);
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new DeviceNotFoundException(request.getDeviceId()));
        Incident incident = incidentRepository.findById(request.getIncidentId())
                .orElseThrow(() -> new IncidentNotFoundException(request.getIncidentId()));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        if (user.getRoles().contains(Role.TECHNICIAN)) {
            throw new IllegalArgumentException("Technik nie może tworzyć ticketów");
        }


        ticket.setDevice(device);
        ticket.setUser(user);
        ticket.setIncident(incident);

        String zmiana = user.getZmiana();
        List<User> technicians = userRepository.findAvailableTechniciansOnShift(Role.TECHNICIAN, zmiana);

        // Wybierz technika z najmniejszym obciążeniem
        Optional<User> selectedTechnician = technicians.stream()
                .filter(User::isLoggedIn)
                .min(Comparator.comparingInt(User::getCurrentLoad));

        if (selectedTechnician.isEmpty()) {
            System.out.println("Brak dostępnych techników na zmianie: " + zmiana);
            ticket.setStatus(Status.OCZEKUJACE);
        } else {
            User technician = selectedTechnician.get();
            System.out.println("Przypisano technika: " + technician.getUserName() + " (load: " + technician.getCurrentLoad() + ")");
            ticket.setTechnician(technician);

            // Zwiększa obciążenie technika
            technician.setCurrentLoad(technician.getCurrentLoad() + 1);
            userRepository.save(technician);

            ticket.setStatus(Status.NOWE);
        }


        Ticket saved = ticketRepository.save(ticket);

        return ticketMapper.toResponse(saved);
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
    }

    public TicketResponse updateTicket(Long id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(request.getStatus());

        ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }

    public List<TicketResponse> findTicketsByCriteria(Status status, String user, String device, String titleContains) {
        Specification<Ticket> spec = TicketSpecifications.withFilters(status, user, device, titleContains);
        List<Ticket> filteredTickets = ticketRepository.findAll(spec);

        return filteredTickets.stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    public TicketResponse startTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        ticket.setStatus(Status.W_TRAKCIE);
        ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }
    public TicketResponse finishTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(id));
        ticket.setStatus(Status.ZAMKNIETE);
        ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }
    public List<TicketResponse> getPendingTickets() {
        return ticketRepository.findByStatusIn(List.of(Status.NOWE, Status.W_TRAKCIE)).stream()
                .map(ticketMapper::toResponse)
                .toList();
    }
    public List<TicketResponse> getTicketsByUsername(String username) {
        List<Ticket> tickets = ticketRepository.findByUserUserName(username);
        return tickets.stream()
                .map(ticketMapper::toResponse)
                .toList();
    }
    public TicketResponse assignTechnician(Long ticketId, Long technicianId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
        User technician = userRepository.findById( technicianId)
                .orElseThrow(() -> new UserNotFoundException(technicianId));

        ticket.setTechnician(technician);
        ticket.setStatus(Status.W_TRAKCIE);
        Ticket saved = ticketRepository.save(ticket);
        return ticketMapper.toResponse(saved);
    }
    public List<TicketResponse> getTicketsAssignedTo(String username) {
        return ticketRepository.findByTechnicianUserName(username).stream()
                .map(ticketMapper::toResponse)
                .toList();
    }
}
