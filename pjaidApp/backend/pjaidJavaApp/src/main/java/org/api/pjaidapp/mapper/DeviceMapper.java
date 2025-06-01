package org.api.pjaidapp.mapper;


import org.api.pjaidapp.dto.DeviceDto;
import org.api.pjaidapp.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface DeviceMapper {
    @Mapping(source = "qrCode", target = "qrCode")
    DeviceDto toDto(Device device);
    Device toEntity(DeviceDto deviceDto);

}

