package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.UserDto;
import org.api.pjaidapp.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}

