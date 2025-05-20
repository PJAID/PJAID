package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.model.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toEntity(TicketRequest request);

    @Mapping(source = "technician.userName", target = "technicianName")
    @Mapping(source = "technician.id", target = "technicianId")

    @Mapping(source = "device", target = "device")
    @Mapping(source = "user", target = "user")
    TicketResponse toResponse(Ticket ticket);
}
