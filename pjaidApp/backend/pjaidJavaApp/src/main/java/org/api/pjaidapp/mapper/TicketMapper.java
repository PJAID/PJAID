package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.TicketRequest;
import org.api.pjaidapp.dto.TicketResponse;
import org.api.pjaidapp.model.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DeviceMapper.class, UserMapper.class})
public interface TicketMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "device", ignore = true)
    @Mapping(target = "user", ignore = true)
    Ticket toEntity(TicketRequest request);

    @Mapping(source = "device", target = "device")
    @Mapping(source = "user", target = "user")
    TicketResponse toResponse(Ticket ticket);
}
