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
        String subject = "Recuperação de Senha";
        String body = "Para redefinir sua senha, clique no link: http://cinelist.com/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setFrom("duckbilltech@gmail.com"); // Email de remetente
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message); // mailSender é um JavaMailSender configurado
    }
}