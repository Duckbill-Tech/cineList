package com.duckbill.cine_list.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String userEmail, String token) {
        if (userEmail == null || userEmail.isEmpty()) {
            logger.warn("Email não fornecido. Ignorando envio de email.");
            return;
        }
        if (token == null || token.isEmpty()) {
            logger.warn("Token não fornecido. Ignorando envio de email.");
            return;
        }

        String subject = "Recuperação de Senha";
        String body = "Para redefinir sua senha, copie o token abaixo e insira no formulário de redefinição:\n\n" +
                "Token: " + token + "\n\n" +
                "Acesse: http://cinelist.com/reset-password para redefinir sua senha.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setFrom("duckbilltech@gmail.com");
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            logger.info("Email enviado com sucesso para {}", userEmail);
        } catch (Exception e) {
            logger.error("Erro ao enviar email para {}: {}", userEmail, e.getMessage());
        }
    }
}