package org.api.pjaidapp.exception;

public class TicketNotFoundException extends AbstractNotFoundException{
    public TicketNotFoundException(int id) {
        super("Ticket with id " + id + " not found");
    }
}
