package org.api.pjaidapp.dto;

import lombok.Data;

@Data
public class IncidentDto {
    private Long id;
    private String title;
    private String priority;
}
