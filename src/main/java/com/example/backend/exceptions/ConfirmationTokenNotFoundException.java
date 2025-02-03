package com.example.backend.exceptions;

public class ConfirmationTokenNotFoundException extends RuntimeException{
    public ConfirmationTokenNotFoundException(String message){
        super(message);
    }
}
