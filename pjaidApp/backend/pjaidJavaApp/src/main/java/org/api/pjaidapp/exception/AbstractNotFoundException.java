package org.api.pjaidapp.exception;

public abstract class AbstractNotFoundException extends RuntimeException {
    protected AbstractNotFoundException(String message) {
        super(message);
    }
}
