package org.api.pjaidapp.mapper;


import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface DeviceMapper {
    @Mapping(source = "qrCode", target = "qrCode")
    @Mapping(source = "status", target = "status")
    DeviceDto toDto(Device device);

    @Mapping(source = "status", target = "status")
    Device toEntity(DeviceDto deviceDto);



}

