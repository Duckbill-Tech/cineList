package com.duckbill.cine_list.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class EmailService {

//    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
//
//    private final String sendGridApiKey;
//    private final String fromEmail;
//    private final URL sendGridUrl;
//
//    // Construtor anotado com @Value para injeção de valores
//    public EmailService(
//            @Value("${spring.sendgrid.api-key}") String sendGridApiKey,
//            @Value("${spring.sendgrid.from-email}") String fromEmail,
//            @Value("${sendgrid.url}") String sendGridUrl) throws Exception {
//        this.sendGridApiKey = sendGridApiKey;
//        this.fromEmail = fromEmail;
//        this.sendGridUrl = new URL(sendGridUrl);
//    }
//
//    public void sendPasswordResetEmail(String userEmail, String token) {
//        if (userEmail == null || userEmail.isEmpty()) {
//            logger.warn("Email não fornecido. Ignorando envio de email.");
//            return;
//        }
//        if (token == null || token.isEmpty()) {
//            logger.warn("Token não fornecido. Ignorando envio de email.");
//            return;
//        }
//
//        String subject = "Recuperação de Senha";
//        String body = "Para redefinir sua senha, copie o token abaixo e insira no formulário de redefinição:\n\n" +
//                "Token: " + token + "\n\n" +
//                "Acesse: http://cinelist.com/reset-password para redefinir sua senha.";
//
//        try {
//            sendEmailViaSendGrid(userEmail, subject, body);
//            logger.info("Email enviado com sucesso para {}", userEmail);
//        } catch (Exception e) {
//            logger.error("Erro ao enviar email para {}: {}", userEmail, e.getMessage());
//        }
//    }
//
//    private void sendEmailViaSendGrid(String toEmail, String subject, String content) throws Exception {
//        HttpURLConnection conn = (HttpURLConnection) sendGridUrl.openConnection();
//
//        conn.setDoOutput(true);
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Authorization", "Bearer " + sendGridApiKey);
//        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//
//        String jsonInputString = "{"
//                + "\"personalizations\": [{\"to\": [{\"email\": \"" + toEmail + "\"}]}],"
//                + "\"from\": {\"email\": \"" + fromEmail + "\"},"
//                + "\"subject\": \"" + subject + "\","
//                + "\"content\": [{\"type\": \"text/plain\", \"value\": \"" + content + "\"}]"
//                + "}";
//
//        try (OutputStream os = conn.getOutputStream()) {
//            byte[] input = jsonInputString.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        int responseCode = conn.getResponseCode();
//        if (responseCode != HttpURLConnection.HTTP_ACCEPTED && responseCode != HttpURLConnection.HTTP_OK) {
//            throw new Exception("Erro ao enviar email. Código de resposta: " + responseCode);
//        }
//
//        logger.debug("Resposta da API SendGrid: Código {}", responseCode);
//    }
}