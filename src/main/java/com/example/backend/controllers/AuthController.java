package com.example.backend.controllers;

import com.example.backend.dto.UserDto;
import com.example.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto userToRegisterDto = new UserDto(userDto.getUsername(),userDto.getEmail(),userDto.getPassword());
        return new ResponseEntity<>(userService.register(userToRegisterDto), HttpStatus.CREATED);
    }
}
