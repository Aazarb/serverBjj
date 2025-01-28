package com.example.backend.utils.validators;

import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.ValidationException;

import java.util.regex.Pattern;

public class UserDtoValidator {

    private UserDtoValidator() {}

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    // Vérifie tous les champs obligatoires pour l'inscription
    public static void validateForRegistration(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().trim().isEmpty()) {
            throw new ValidationException("Username is required.");
        }

        if (userDto.getEmail() == null || !Pattern.matches(EMAIL_REGEX, userDto.getEmail())) {
            throw new ValidationException("Invalid email format.");
        }

        if (userDto.getPassword() == null || !Pattern.matches(PASSWORD_REGEX, userDto.getPassword())) {
            throw new ValidationException("Password must be at least 8 characters long and contain letters and numbers.");
        }
    }

}

