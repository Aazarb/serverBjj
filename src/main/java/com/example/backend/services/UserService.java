package com.example.backend.services;

import com.example.backend.dto.UserDto;


public interface UserService {
     UserDto register(UserDto userToRegister);
}