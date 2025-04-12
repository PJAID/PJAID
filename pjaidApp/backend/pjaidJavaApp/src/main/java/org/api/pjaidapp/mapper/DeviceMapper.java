package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.model.Device;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    DeviceDto toDto(Device device);
    Device toEntity(DeviceDto deviceDto);
}

