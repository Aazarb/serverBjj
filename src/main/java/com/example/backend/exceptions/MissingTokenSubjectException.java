package com.example.backend.exceptions;

public class MissingTokenSubjectException extends RuntimeException{
    public MissingTokenSubjectException(String message){
        super(message);
    }
}
