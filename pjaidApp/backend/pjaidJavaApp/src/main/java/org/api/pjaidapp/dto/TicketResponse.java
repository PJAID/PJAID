package org.api.pjaidapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.pjaidapp.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private int id;
    private String title;
    private String description;
    private Status status;
    private DeviceDto device;
    private UserDto user;
    private Long technicianId;
    private String technicianName;
    private UserDto technician;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private IncidentDto incident;

}
