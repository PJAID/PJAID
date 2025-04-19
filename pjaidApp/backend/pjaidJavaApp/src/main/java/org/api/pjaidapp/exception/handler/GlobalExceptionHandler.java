package org.api.pjaidapp.exception.handler;

import org.api.pjaidapp.dto.ErrorResponse;
import org.api.pjaidapp.exception.AbstractNotFoundException;
import org.api.pjaidapp.exception.QrCodeGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AbstractNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(AbstractNotFoundException ex) {
        log.warn("Not found: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<Object> handleQrGeneration(QrCodeGenerationException ex) {
        log.error("QR code generation error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleOther(RuntimeException ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse body = new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message);
        return new ResponseEntity<>(body, status);
    }
}
