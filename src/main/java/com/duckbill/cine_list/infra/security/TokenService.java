package com.duckbill.cine_list.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.duckbill.cine_list.db.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // Metodo para gerar o token JWT a partir do email do usuário
    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("cine-list-api")                // Nome da aplicação ou API
                    .withSubject(usuario.getEmail())            // Identificador do usuário, por exemplo, o email
                    .withExpiresAt(this.generateExpirationDate()) // Define a data de expiração
                    .sign(algorithm);                          // Assina o token com o algoritmo HMAC256 e a chave secreta
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    // Metodo para validar o token JWT e obter o email do usuário
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("cine-list-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o "subject" (no caso, o email do usuário) do token
        } catch (JWTVerificationException exception) {
            // Token inválido ou expirado
            return null;
        }
    }

    // Gera a data de expiração do token (2 horas a partir da geração)
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}