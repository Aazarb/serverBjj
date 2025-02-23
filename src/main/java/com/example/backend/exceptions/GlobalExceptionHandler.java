package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handlerDuplicateUserException(DuplicateUserException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(ConfirmationTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handlerConfirmationTokenExpiredException(ConfirmationTokenExpiredException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(ConfirmationTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerConfirmationTokenNotFoundException(ConfirmationTokenNotFoundException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponse> handlerIncorrectPasswordException(IncorrectPasswordException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handlerValidationException(ValidationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(MissingTokenSubjectException.class)
    public ResponseEntity<ErrorResponse> handlerMissingTokenSubjectException(MissingTokenSubjectException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(CookieNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerCookieNotFoundException(CookieNotFoundException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, ex.getMessage(), LocalDateTime.now()));
    }

}
