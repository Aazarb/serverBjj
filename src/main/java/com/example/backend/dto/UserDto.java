package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link com.example.backend.models.User}
 */
@AllArgsConstructor
@Getter
@ToString
public class UserDto implements Serializable {
    private final Long id;
    private final String username;
    private final String email;
}