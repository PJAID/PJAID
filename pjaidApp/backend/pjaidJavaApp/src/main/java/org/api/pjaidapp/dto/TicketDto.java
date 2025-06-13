package org.api.pjaidapp.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TicketDto {
    private Long id;
    private String title;
    private String description;
    private String status; // Enum jako String
    private String priority; // Enum jako String

    private BigDecimal latitude;
    private BigDecimal longitude;

    private Long deviceId;
    private String deviceName;

    private Long userId;
    private String userName;

    private Long technicianId;
    private String technicianName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
