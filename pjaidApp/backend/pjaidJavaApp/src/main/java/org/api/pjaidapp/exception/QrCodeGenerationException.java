package org.api.pjaidapp.exception;

public class QrCodeGenerationException extends RuntimeException {
    public QrCodeGenerationException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
