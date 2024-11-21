package com.duckbill.cine_list.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envia um email para o usuário com as instruções para redefinição de senha.
     *
     * @param userEmail O email do usuário.
     * @param token O token de redefinição de senha.
     */
    public void sendPasswordResetEmail(String userEmail, String token) {
        if (userEmail == null || userEmail.isEmpty()) {
            // Loga a ausência de e-mail e retorna sem enviar
            System.err.println("Email não fornecido. Ignorando envio de email.");
            return;
        }
        if (token == null || token.isEmpty()) {
            // Loga a ausência do token e retorna sem enviar
            System.err.println("Token não fornecido. Ignorando envio de email.");
            return;
        }

        String subject = "Recuperação de Senha";
        String body = "Para redefinir sua senha, clique no link: http://cinelist.com/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setFrom("duckbilltech@gmail.com");
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}