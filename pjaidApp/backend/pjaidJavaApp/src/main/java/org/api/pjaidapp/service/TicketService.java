package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.enums.Role;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.exception.DeviceNotFoundException;
import org.api.pjaidapp.exception.TicketNotFoundException;
import org.api.pjaidapp.exception.UserNotFoundException;
import org.api.pjaidapp.mapper.TicketMapper;
import org.api.pjaidapp.model.Device;
import org.api.pjaidapp.model.Ticket;
import org.api.pjaidapp.model.User;
import org.api.pjaidapp.repository.DeviceRepository;
import org.api.pjaidapp.repository.TicketRepository;
import org.api.pjaidapp.repository.UserRepository;
import org.api.pjaidapp.repository.specification.TicketSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final TicketMapper ticketMapper;

    private final ShiftCalendarService shiftCalendarService;


    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, DeviceRepository deviceRepository, TicketMapper ticketMapper, ShiftCalendarService shiftCalendarService) {

        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.ticketMapper = ticketMapper;
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

        Ticket ticket = ticketMapper.toEntity(request);
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new DeviceNotFoundException(request.getDeviceId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));


        ticket.setDevice(device);
        ticket.setUser(user);
        ticket.setPriority(request.getPriority());
        ticket.setLatitude(request.getLatitude());
        ticket.setLongitude(request.getLongitude());

        // Automatyczne przypisanie technika
        String zmiana = user.getZmiana();
        List<User> technicians = userRepository.findAvailableTechniciansOnShift(Role.TECHNICIAN, zmiana);

        // Wybierz technika z najmniejszym obciążeniem
        Optional<User> selectedTechnician = technicians.stream()
                .filter(User::isLoggedIn)
                .min(Comparator.comparingInt(User::getCurrentLoad));

        if (selectedTechnician.isEmpty()) {
            logger.warn("Brak dostępnych techników na zmianie: {}", zmiana);
            ticket.setStatus(Status.OCZEKUJACE);
        } else {
            User technician = selectedTechnician.get();
            logger.info("Przypisano technika: {} (load: {})", technician.getUserName(), technician.getCurrentLoad());
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
        ticket.setPriority(request.getPriority());
        ticket.setLatitude(request.getLatitude());
        ticket.setLongitude(request.getLongitude());

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

    public List<TicketResponse> getTicketsByUserOrTechnician(String username) {
        Pageable pageable = PageRequest.of(0, 50);
        List<Ticket> tickets = ticketRepository.findByUserUserNameOrTechnicianUserName(username, username, pageable)
                .getContent();
        return tickets.stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    public Page<TicketResponse> getTicketsByUserOrTechnicianPaged(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> paged = ticketRepository.findByUserUserNameOrTechnicianUserName(username, username, pageable);
        return paged.map(ticketMapper::toResponse);
    }

    public Page<TicketResponse> getAllTicketsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> paged = ticketRepository.findAll(pageable);
        return paged.map(ticketMapper::toResponse);
    }


    public Map<String, Long> getTicketStatusSummary() {
        return ticketRepository.findAll().stream()
                .collect(Collectors.groupingBy(t -> t.getStatus().name(), Collectors.counting()));
    }
    public TicketResponse updateTicketStatus(Long id, Status newStatus) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);

    }

    public TicketResponse updateTicketAssignee(Long id, String assigneeName) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        User technician = userRepository.findByUserName(assigneeName)
                .orElseThrow(() -> new UserNotFoundException("Technician with username " + assigneeName + " not found"));

        ticket.setTechnician(technician);
        ticketRepository.save(ticket);

        return ticketMapper.toResponse(ticket);
    }

    public byte[] generateCsvReportForTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        StringBuilder sb = new StringBuilder();
        sb.append("ID;Tytuł;Opis;Status;Technik;Data utworzenia\n");

        sb.append(ticket.getId()).append(";")
                .append(safe(ticket.getTitle())).append(";")
                .append(safe(ticket.getDescription())).append(";")
                .append(ticket.getStatus() != null ? ticket.getStatus().name() : "Brak").append(";")
                .append(ticket.getTechnician() != null ? ticket.getTechnician().getUserName() : "Brak").append(";")
                .append(ticket.getCreatedAt() != null ? ticket.getCreatedAt().toString() : "Brak")
                .append("\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String safe(String text) {
        return text == null ? "" : text.replace(";", ",");
    }





}
