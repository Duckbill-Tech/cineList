package com.duckbill.cine_list.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testSendPasswordResetEmailWithNullEmail() {
        String token = "valid-token";

        // Não deve lançar exceção
        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail(null, token));
    }

    @Test
    void testSendPasswordResetEmailWithEmptyToken() {
        String email = "user@example.com";

        // Não deve lançar exceção
        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail(email, ""));
    }

    @Test
    void testSendPasswordResetEmailValid() {
        String email = "user@example.com";
        String token = "valid-token";

        // Simula o comportamento esperado do JavaMailSender
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendPasswordResetEmail(email, token);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}