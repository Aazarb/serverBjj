package com.example.backend.services.implementations;

import com.example.backend.services.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, String confirmationLink, String userName) {

        String subject = "Welcome to FullBjj – Confirm Your Registration";
        String body = "Hello " + userName + ",\n\n" +
                "Thank you for signing up for FullBjj – your new platform dedicated to Brazilian Jiu-Jitsu.\n\n" +
                "To activate your account, please click the link below:\n" +
                confirmationLink + "\n\n" +
                "This link is valid for 30 minutes.\n\n" +
                "If you did not initiate this registration, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The FullBjj Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fullbjj@gmx.fr");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}