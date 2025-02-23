package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String token;
    private UserDto userDto;

    public LoginResponseDto(String token, UserDto userDto) {
        this.token = token;
        this.userDto = userDto;
    }
}

