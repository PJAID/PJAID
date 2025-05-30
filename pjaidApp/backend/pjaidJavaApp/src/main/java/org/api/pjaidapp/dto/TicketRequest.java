package org.api.pjaidapp.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.api.pjaidapp.enums.Status;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {

    private String title;
    private String description;
    private Status status;
    private Long deviceId;
    private Long userId;
    private Long locationX;
    private Long locationY;
}
