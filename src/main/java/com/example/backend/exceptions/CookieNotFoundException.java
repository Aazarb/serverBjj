package com.example.backend.exceptions;

public class CookieNotFoundException extends Exception {
    public CookieNotFoundException(String message) {
        super(message);
    }
}
