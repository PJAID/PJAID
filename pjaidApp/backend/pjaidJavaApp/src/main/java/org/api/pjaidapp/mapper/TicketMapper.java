package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.model.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toEntity(TicketRequest request);

    TicketResponse toResponse(Ticket ticket);

    default String mapStatusToString(Enum<?> status) {
        return status != null ? status.name() : null;
    }

}