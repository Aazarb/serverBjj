package com.example.backend.dto;

import com.example.backend.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.io.Serializable;

/**
 * DTO for {@link com.example.backend.models.User}
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@ToString
public class UserDto implements Serializable {
    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final RoleEnum role;

    public UserDto(String username, String email, String password) {
        this.id = null;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = RoleEnum.MEMBER;
    }

    public UserDto(Long id, String username, String email, RoleEnum role) {
        this.id = id;
        this.username = username;
        this.password = null;
        this.email = email;
        this.role = role;
    }

}