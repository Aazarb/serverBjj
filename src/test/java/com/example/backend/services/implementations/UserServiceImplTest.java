package com.example.backend.services.implementations;

import com.example.backend.dto.UserDto;
import com.example.backend.enums.RoleEnum;
import com.example.backend.exceptions.DuplicateUserException;
import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class UserServiceImplTest {

    @Mock
    private UserRepository userRepository; // Mock du repository

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerNewValidUser() {
        UserDto userDto = new UserDto("JohnDoe", "john.doe@gmail.com", "Pas@3578", RoleEnum.MEMBER);

        when(this.userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(this.userRepository.findByEmail(userDto.getUsername())).thenReturn(Optional.empty());
        when(this.bCryptPasswordEncoder.encode(userDto.getPassword())).thenReturn("hashedPas@357");

        User savedUser = new User(1L, userDto.getUsername(), userDto.getEmail(), "hashedPassword123", userDto.getRole());
        when(this.userRepository.save(any(User.class))).thenReturn(savedUser);


        UserDto res = this.userService.register(userDto);

        assertNotNull(res);
        assertNotNull(res.getId());
        assertEquals(savedUser.getUsername(), res.getUsername());
        assertEquals(savedUser.getEmail(), res.getEmail());
        assertEquals(savedUser.getRole(), res.getRole());
    }

    @Test
    void registerUsernameAlreadyExists() {
        UserDto userDto = new UserDto("JohnDoe", "john.doe@gmail.com", "Pas@3578", RoleEnum.MEMBER);

        when(this.userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(this.userRepository.findByEmail(userDto.getUsername())).thenReturn(Optional.empty());
        when(this.bCryptPasswordEncoder.encode(userDto.getPassword())).thenReturn("hashedPas@357");

        User savedUser = new User(1L, userDto.getUsername(), "test@mail.com", "hashedPassword123", userDto.getRole());
        when(this.userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(savedUser));

        Assertions.assertThrows(DuplicateUserException.class,()-> this.userService.register(userDto));
    }

    @Test
    void registerEmailAlreadyExists() {
        UserDto userDto = new UserDto("JohnDoe", "john.doe@gmail.com", "Pas@3578", RoleEnum.MEMBER);

        when(this.userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(this.userRepository.findByEmail(userDto.getUsername())).thenReturn(Optional.empty());
        when(this.bCryptPasswordEncoder.encode(userDto.getPassword())).thenReturn("hashedPas@357");

        User savedUser = new User(1L, "PaulP", "john.doe@gmail.com", "hashedPassword123", userDto.getRole());
        when(this.userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(savedUser));

        Assertions.assertThrows(DuplicateUserException.class,()-> this.userService.register(userDto));
    }
}