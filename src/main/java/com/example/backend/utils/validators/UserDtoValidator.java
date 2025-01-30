package com.example.backend.utils.validators;

import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.ValidationException;

import java.util.regex.Pattern;

public class UserDtoValidator {

    private UserDtoValidator() {}

    private static final String USERNAME_REGEX = "^(?![_.])[a-zA-Z0-9._]{3,20}(?<![_.])$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@]{8,}$"; // Caratères interdits (& %  / " ')

    public static final String USERNAME_ERROR_MESSAGE = "Username must be 3-20 characters long, can only contain letters, numbers, underscores, and dots, but cannot start or end with an underscore or dot.";
    public static final String EMAIL_ERROR_MESSAGE = "Invalid email format.";
    public static final String PASSWORD_ERROR_MESSAGE = "Password must be at least 8 characters long, contain at least one letter and one number, and must not include special characters.";

    // Vérifie tous les champs obligatoires pour l'inscription
    public static void validateForRegistration(UserDto userDto) throws ValidationException {
        if (userDto.getUsername() == null || userDto.getUsername().trim().isEmpty() || userDto.getPassword() == null
                || !Pattern.matches(USERNAME_REGEX, userDto.getUsername())) {
            throw new ValidationException(USERNAME_ERROR_MESSAGE);
        }

        if (userDto.getEmail() == null || !Pattern.matches(EMAIL_REGEX, userDto.getEmail())) {
            throw new ValidationException(EMAIL_ERROR_MESSAGE);
        }

        if (userDto.getPassword() == null || !Pattern.matches(PASSWORD_REGEX, userDto.getPassword())) {
            throw new ValidationException(PASSWORD_ERROR_MESSAGE);
        }
    }

}

