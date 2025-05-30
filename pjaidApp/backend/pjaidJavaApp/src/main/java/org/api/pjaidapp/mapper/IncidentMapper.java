package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.IncidentDto;
import org.api.pjaidapp.model.Incident;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncidentMapper {
    IncidentDto toDto(Incident incident);
}

