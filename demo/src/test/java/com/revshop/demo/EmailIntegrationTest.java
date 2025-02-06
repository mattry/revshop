package com.revshop.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.revshop.demo.service.EmailService;

@SpringBootTest
public class EmailIntegrationTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        // Arrange
        String to = "test@example.com";
        String subject = "Test Email";
        String message = "This is a test email.";

        // Act
        emailService.sendEmail(to, subject, message);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // ✅ Ensures email sending was called
    }
}
