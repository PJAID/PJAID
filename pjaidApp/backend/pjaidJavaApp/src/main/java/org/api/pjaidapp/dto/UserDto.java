package org.api.pjaidapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String email;
    private String role;
    private boolean active;
    private int currentLoad;
    private String zmiana;
    private boolean loggedIn;
    private String lastLoginDate;
}
