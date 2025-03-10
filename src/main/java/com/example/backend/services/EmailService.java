package com.example.backend.services;

public interface EmailService {
    void sendVerificationEmail(String to, String subject, String body);
    void sendConfirmationEmail(String to, String username);
}