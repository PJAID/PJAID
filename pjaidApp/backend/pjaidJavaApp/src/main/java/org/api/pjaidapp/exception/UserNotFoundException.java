package org.api.pjaidapp.exception;

public class UserNotFoundException extends AbstractNotFoundException {
    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
    public UserNotFoundException(String username) {
        super("User with username " + username + " not found");
    }
}
