package com.example.backend.services;

import com.example.backend.dto.UserDto;
import com.example.backend.models.User;

import java.util.Optional;


public interface UserService {
     UserDto register(UserDto userToRegister);
     Optional<User> loadUser(String username);
}