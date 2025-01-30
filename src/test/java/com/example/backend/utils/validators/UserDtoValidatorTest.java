package com.example.backend.utils.validators;

import com.example.backend.dto.UserDto;
import com.example.backend.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static com.example.backend.utils.validators.UserDtoValidator.validateForRegistration;
import static org.junit.jupiter.api.Assertions.*;
class UserDtoValidatorTest {

    @Test
    void validateUsernameContainsForbiddenCharacterThrowsValidationException() {
        UserDto user = new UserDto("*JohnDoe", "john.doe@gmail.com", "Hello@123");

        Exception exception = assertThrows(ValidationException.class, () -> validateForRegistration(user));
        assertEquals(UserDtoValidator.USERNAME_ERROR_MESSAGE, exception.getMessage());
    }
    @Test
    void validateUsernameTooLongThrowsValidationException() {
        UserDto user = new UserDto("JohnDoeDoeDoeDoeDoeDoe", "john.doe@gmail.com", "Hello@123");

        Exception exception = assertThrows(ValidationException.class, () -> validateForRegistration(user));
        assertEquals(UserDtoValidator.USERNAME_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void validateEmailInvalidFormatThrowsValidationException() {
        UserDto user = new UserDto("JohnDoe", "john.doegmail.com", "Hello@123");

        Exception exception = assertThrows(ValidationException.class, () -> validateForRegistration(user));
        assertEquals(UserDtoValidator.EMAIL_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void validatePasswordContainsForbiddenCharactersThrowsValidationException() {
        UserDto user = new UserDto("JohnDoe", "john.doe@gmail.com", "Hello@&+123");

        Exception exception = assertThrows(ValidationException.class, () -> validateForRegistration(user));
        assertEquals(UserDtoValidator.PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void validate_PasswordTooShort_ThrowsValidationException() {
        UserDto user = new UserDto("JohnDoe", "john.doe@gmail.com", "Pass@1");

        Exception exception = assertThrows(ValidationException.class, () -> validateForRegistration(user));
        assertEquals(UserDtoValidator.PASSWORD_ERROR_MESSAGE, exception.getMessage());
    }
}