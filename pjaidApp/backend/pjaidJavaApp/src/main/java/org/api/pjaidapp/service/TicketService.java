package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Service
public class TicketService {

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
        Ticket ticket = ticketMapper.toEntity(request);
        Device device = deviceRepository.findById(request.getDeviceId())
                .orElseThrow(() -> new DeviceNotFoundException(request.getDeviceId()));


        String zmiana = shiftCalendarService.getShiftForDate(LocalDate.now());

        List<User> techniciansOnShift = userRepository.findByRoleAndActiveTrueAndZmianaOrderByCurrentLoadAsc("TECHNICIAN", zmiana);
        for (User technician : techniciansOnShift) {
            if (technician.isLoggedIn()) {
                return assignTicket(ticket, device, technician);
            }
        }

        List<User> otherTechs = userRepository.findByRoleAndActiveTrueOrderByCurrentLoadAsc("TECHNICIAN");
        for (User technician : otherTechs) {
            if (!technician.getZmiana().equals(zmiana) && technician.isLoggedIn()) {
                return assignTicket(ticket, device, technician);
            }
        }

        throw new UserNotFoundException("Brak dostępnego technika");

    }


    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }
        ticketRepository.deleteById(id);
    }

    public void completeTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));


        ticket.setStatus(Status.ZAMKNIETE);

        User technician = ticket.getUser();
        technician.setCurrentLoad(technician.getCurrentLoad() - 1);
        technician.getTicketQueue().remove(ticket);

        ticketRepository.save(ticket);
        userRepository.save(technician);
    }

    public TicketResponse updateTicket(Long id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        Status currentStatus = ticket.getStatus();
        Status newStatus = request.getStatus();

        if (currentStatus == Status.ZAMKNIETE && newStatus != Status.ZAMKNIETE) {
            throw new IllegalStateException("Nie można zmienić statusu zamkniętego zgłoszenia.");
        }


        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setStatus(request.getStatus());

        Ticket saved = ticketRepository.save(ticket);
        return ticketMapper.toResponse(saved);


    }

    public List<TicketResponse> findTicketsByCriteria(Status status, String user, String device, String
            titleContains) {
        Specification<Ticket> spec = TicketSpecifications.withFilters(status, user, device, titleContains);
        List<Ticket> filteredTickets = ticketRepository.findAll(spec);

        return filteredTickets.stream()
                .map(ticketMapper::toResponse)
                .toList();
    }
    private TicketResponse assignTicket(Ticket ticket, Device device, User technician) {
    ticket.setDevice(device);
    ticket.setUser(technician);
    ticket.setStatus(Status.NOWE);

    technician.setCurrentLoad(technician.getCurrentLoad() + 1);
    technician.getTicketQueue().add(ticket);

    userRepository.save(technician);
    Ticket saved = ticketRepository.save(ticket);

    return ticketMapper.toResponse(saved);
    }
}

