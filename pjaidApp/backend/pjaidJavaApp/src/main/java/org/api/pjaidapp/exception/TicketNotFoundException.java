package org.api.pjaidapp.exception;

public class TicketNotFoundException extends AbstractNotFoundException{
    public TicketNotFoundException(Long id) {
        super("Ticket with id " + id + " not found");
    }
}
