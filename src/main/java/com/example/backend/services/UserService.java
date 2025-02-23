package com.example.backend.services;

import com.example.backend.dto.LoginResponseDto;
import com.example.backend.dto.LoggedStatusDto;
import com.example.backend.dto.UserDto;



public interface UserService {
     UserDto register(UserDto userToRegister);
     void confirmRegistration(String token);
     LoginResponseDto login(UserDto userDto);
     LoggedStatusDto checkStatusAndRole(String token);
}