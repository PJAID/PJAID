package org.api.pjaidapp.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.pjaidapp.enums.Priority;
import org.api.pjaidapp.enums.Status;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {

    private String title;
    private String description;
    private Status status;
    private Priority priority = Priority.MEDIUM;
    private Long deviceId;
    private String userName;
    private Long incidentId;
    private Long userId;
    private BigDecimal latitude;
    private BigDecimal longitude;

}
