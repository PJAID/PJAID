package org.api.pjaidapp.exception;

public class DeviceNotFoundException extends AbstractNotFoundException {
    public DeviceNotFoundException(Long id) {
        super("Device with id " + id + " not found");
    }
}
