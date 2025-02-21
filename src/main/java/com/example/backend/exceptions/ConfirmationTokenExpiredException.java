package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ConfirmationTokenExpiredException extends RuntimeException{
    public ConfirmationTokenExpiredException(String message) {
        super(message);
    }
}
