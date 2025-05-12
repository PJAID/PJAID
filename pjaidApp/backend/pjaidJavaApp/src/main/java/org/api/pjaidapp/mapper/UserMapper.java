package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.UserDto;
import org.api.pjaidapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "currentLoad", ignore = true)
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}

