package com.onboard.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String to, String adminName, String password, String companyName) {
        String platformName = "QuickOnboard AI";
        String subject = "Welcome to " + platformName + " - Registration Successful";
        
        String body = String.format(
            "Hello %s,\n\n" +
            "Congratulations! Your company, %s, has been successfully onboarded onto the %s platform.\n\n" +
            "Here are your administrator login credentials:\n" +
            "Email: %s\n" +
            "Temporary Password: %s\n\n" +
            "Please login and change your password immediately for security reasons.\n\n" +
            "Best Regards,\n" +
            "Team %s",
            adminName, companyName, platformName, to, password, platformName
        );

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@quickonboard.ai");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);
            logger.info("Welcome email successfully sent to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send welcome email to {}: {}", to, e.getMessage());
            // In a real production system, you might want to retry or use a dead-letter queue.
        }
    }
}
