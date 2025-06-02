package org.api.pjaidapp.mapper;

import org.api.pjaidapp.dto.UserDto;
import org.api.pjaidapp.enums.Role;
import org.api.pjaidapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        imports = {org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.class})
public interface UserMapper {

    @Mapping(target = "password", expression = "java(new BCryptPasswordEncoder().encode(userDto.getPassword()))")
    @Mapping(target = "roles", expression = "java(mapRoles(userDto.getRoles()))")
    User toEntity(UserDto userDto);

    @Mapping(target = "password", constant = "")
    @Mapping(target = "roles", expression = "java(mapRolesToString(user.getRoles()))")
    UserDto toDto(User user);

    default Set<Role> mapRoles(Set<String> roleStrings) {
        if (roleStrings == null) return Set.of();
        return roleStrings.stream()
                .map(String::toUpperCase)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    default Set<String> mapRolesToString(Set<Role> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(Role::name)
                .collect(Collectors.toSet());
    }
}

