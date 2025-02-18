package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ConfirmationTokenNotFoundException extends RuntimeException{
    public ConfirmationTokenNotFoundException(String message){
        super(message);
    }
}
