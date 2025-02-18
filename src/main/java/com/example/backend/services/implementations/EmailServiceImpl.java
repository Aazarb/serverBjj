package com.example.backend.services.implementations;

import com.example.backend.services.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private static final String LOGIN_LINK = "www.fullbjj.com/login";
    private static final String SUPPORT_EMAIL = "support@fullbjj.com";
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

        sendMessage("fullbjj@gmx.fr",to,subject,body);
    }

    @Override
    public void sendConfirmationEmail(String to,String userName) {
        String subject = "Registration Confirmed – Welcome to FullBjj!";
        String body = "Hello " + userName + ",\n\n" +
                "We're excited to let you know that your FullBjj account has been successfully activated! Welcome to our vibrant community dedicated to Brazilian Jiu-Jitsu.\n\n" +
                "You now have full access to all our features, including:\n" +
                "- Personalized training plans,\n" +
                "- Expert tips and tutorials,\n" +
                "- A supportive community of fellow enthusiasts.\n\n" +
                "Start exploring today by logging in here: " + LOGIN_LINK + "\n\n" +
                "If you have any questions or need assistance, feel free to reach out to us at " + SUPPORT_EMAIL + ". We're here to help!\n\n" +
                "Thank you for joining FullBjj. We look forward to being part of your Brazilian Jiu-Jitsu journey.\n\n" +
                "Best regards,\n" +
                "The FullBjj Team";

        sendMessage("fullbjj@gmx.fr",to,subject,body);
    }

    private void sendMessage(String from,String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}