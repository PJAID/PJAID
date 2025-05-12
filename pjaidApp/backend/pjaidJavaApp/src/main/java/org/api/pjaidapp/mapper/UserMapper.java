package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.UserDto;
import org.api.pjaidapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        imports = {org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.class})
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "password", expression = "java(new BCryptPasswordEncoder().encode(userDto.getPassword()))")
    User toEntity(UserDto userDto);
}

