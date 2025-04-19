package org.api.pjaidapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    LocalDateTime timestamp;
    int status;
    String error;
    String message;
}
