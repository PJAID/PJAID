package org.api.pjaidapp.controller;

import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.dto.UserDto;
import org.api.pjaidapp.enums.DeviceStatus;
import org.api.pjaidapp.enums.Priority;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.exception.TicketNotFoundException;
import org.api.pjaidapp.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.api.pjaidapp.enums.Priority.LOW;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private UserDto userDto1;
    private UserDto userDto2;
    private DeviceDto deviceDto1;
    private DeviceDto deviceDto2;

    private TicketResponse ticketResponse1;
    private TicketResponse ticketResponse2;
    private TicketRequest ticketRequestForCreate;
    private TicketRequest ticketRequestForUpdate;

    @BeforeEach
    void setUp() {
        userDto1 = new UserDto(1L, "Jan Kowalski", "jan@domain.com", "Qwerty.1", new HashSet<>(), true, true, "TAK", 1);
        userDto2 = new UserDto(2L, "Anna Nowak", "anna@domain.com", "Qwerty.1", new HashSet<>(), true, true, "TAK", 1);
        deviceDto1 = new DeviceDto(1L, "Laptop Dell", "SN123", "20240101", "20250101", "XDD", Status.NOWE, 12D, 13D, DeviceStatus.MAINTENANCE.toString());
        deviceDto2 = new DeviceDto(2L, "Drukarka HP", "SN456", "20240101", "20250101", "XDDD", Status.NOWE, 12D, 13D, DeviceStatus.FAULTY.toString());

        ticketResponse1 = new TicketResponse(1L, "Problem A", "Opis A", Status.NOWE, LOW, BigDecimal.valueOf(50.061), BigDecimal.valueOf(19.938),          // longitude
                deviceDto1, userDto1, 1L, "technic", userDto1, LocalDateTime.now(), LocalDateTime.now(), "Jan Kowalski");
        ticketResponse2 = new TicketResponse(2L, "Problem B", "Opis B", Status.NOWE, LOW, BigDecimal.valueOf(50.061), BigDecimal.valueOf(19.938), deviceDto2, userDto2, 1L, "technic", userDto2, LocalDateTime.now(), LocalDateTime.now(), "Jan Kowalski");

        ticketRequestForCreate = new TicketRequest("Nowy problem", "Opis nowego problemu", Status.NOWE, Priority.MEDIUM, 1L, "Technik", 1L, 2L, BigDecimal.valueOf(50.061), BigDecimal.valueOf(19.938));
        ticketRequestForUpdate = new TicketRequest("Zaktualizowany tytuł", "Zaktualizowany opis", Status.NOWE, Priority.MEDIUM, 2L, "Technik_2", 2L, 2L, BigDecimal.valueOf(50.061), BigDecimal.valueOf(19.938));

        lenient().when(ticketService.getTicketById(1L)).thenReturn(ticketResponse1);
        lenient().when(ticketService.findTicketsByCriteria(isNull(), isNull(), isNull(), isNull()))
                .thenReturn(Arrays.asList(ticketResponse1, ticketResponse2));
        lenient().when(ticketService.getAllActiveTickets())
                .thenReturn(Collections.singletonList(ticketResponse1));
    }

    @Test
    void getTicketById_shouldReturnOkAndTicket_whenTicketExists() {
        ResponseEntity<TicketResponse> response = ticketController.getTicketById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ticketResponse1, response.getBody());
    }

    @Test
    void getAllTickets_noFilters_shouldReturnAllTickets() {
        ResponseEntity<List<TicketResponse>> response = ticketController.getAllTickets(null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().containsAll(Arrays.asList(ticketResponse1, ticketResponse2)));
    }

    @Test
    void getAllTickets_withStatusFilter_shouldReturnFilteredTickets() {
        when(ticketService.findTicketsByCriteria(eq(Status.NOWE), isNull(), isNull(), isNull()))
                .thenReturn(Collections.singletonList(ticketResponse1));

        ResponseEntity<List<TicketResponse>> response = ticketController.getAllTickets(Status.NOWE, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(Status.NOWE, response.getBody().get(0).getStatus());
    }

    @Test
    void getAllActiveTickets_shouldReturnOkAndActiveTicketList() {
        ResponseEntity<List<TicketResponse>> response = ticketController.getAllActiveTickets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void createTicket_shouldReturnCreatedAndNewTicket() {
        TicketResponse created = new TicketResponse(
                3L,
                ticketRequestForCreate.getTitle(),
                ticketRequestForCreate.getDescription(),
                ticketRequestForCreate.getStatus(),
                Priority.MEDIUM,
                BigDecimal.valueOf(50.061),
                BigDecimal.valueOf(19.938),
                deviceDto1,
                userDto1,
                1L,
                "technic",
                userDto1,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Test"
        );
        when(ticketService.createTicket(any(TicketRequest.class))).thenReturn(created);

        ResponseEntity<TicketResponse> response = ticketController.createTicket(ticketRequestForCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(created, response.getBody());
    }

    @Test
    void updateTicket_shouldReturnOkAndUpdatedTicket() {
        TicketResponse updated = new TicketResponse(
                1L,
                ticketRequestForUpdate.getTitle(),
                ticketRequestForUpdate.getDescription(),
                ticketRequestForUpdate.getStatus(),
                Priority.MEDIUM,
                BigDecimal.valueOf(50.061),
                BigDecimal.valueOf(19.938),
                deviceDto2,
                userDto2,
                1L,
                "technic2",
                userDto2,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "Test"
        );
        when(ticketService.updateTicket(eq(1L), any(TicketRequest.class))).thenReturn(updated);

        ResponseEntity<TicketResponse> response = ticketController.updateTicket(1L, ticketRequestForUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void deleteTicket_shouldReturnNoContent() {
        doNothing().when(ticketService).deleteTicket(1L);

        ResponseEntity<Void> response = ticketController.deleteTicket(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getTicketById_nonExisting_shouldReturnNotFound() {
        Long id = 999L;
        when(ticketService.getTicketById(id))
                .thenThrow(new TicketNotFoundException(id));

        TicketNotFoundException ex = assertThrows(
                TicketNotFoundException.class,
                () -> ticketController.getTicketById(id)
        );

        assertEquals("Ticket with id 999 not found", ex.getMessage());
    }

    @Test
    void getAllTickets_noMatches_shouldReturnEmptyList() {
        when(ticketService.findTicketsByCriteria(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<TicketResponse>> response = ticketController.getAllTickets(Status.ZAMKNIETE, null, null, null);

        assertTrue(response.getBody().isEmpty());
    }


    @Test
    void PasswordGeneratorTest() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Hasło zakodowane: " + encodedPassword);
        assertEquals(true, true);
    }

}