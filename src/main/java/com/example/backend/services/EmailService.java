package com.example.backend.services;

public interface EmailService {
    void sendVerificationEmail(String to, String subject, String body);
}