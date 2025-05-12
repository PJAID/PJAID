package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    @Mapping(target = "qrCode", ignore = true)
    DeviceDto toDto(Device device);
    Device toEntity(DeviceDto deviceDto);
}

