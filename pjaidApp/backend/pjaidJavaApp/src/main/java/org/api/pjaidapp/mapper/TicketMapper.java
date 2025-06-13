package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.*;
import org.api.pjaidapp.model.Device;
import org.api.pjaidapp.model.Ticket;
import org.api.pjaidapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "device", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "technician", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Ticket toEntity(TicketRequest request);

    @Mapping(source = "technician.userName", target = "technicianName")
    @Mapping(source = "technician.id", target = "technicianId")
    @Mapping(source = "device", target = "device")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "id", target = "id")
    TicketResponse toResponse(Ticket ticket);

    @Mapping(source = "qrCode", target = "qrCode")
    DeviceDto toDto(Device device);
    UserDto toDto(User user);


}
