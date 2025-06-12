package org.api.pjaidapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.pjaidapp.enums.Status;
import org.api.pjaidapp.enums.Priority;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private DeviceDto device;
    private UserDto user;
    private Long technicianId;
    private String technicianName;
    private UserDto technician;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String assignee;

}
