package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.dto.*;
import com.duckbill.cine_list.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body, HttpServletResponse response) {
        try {
            // Realiza o login e gera o token
            ResponseDTO responseDTO = usuarioService.login(body.email(), body.senha());

            // Obtém o token JWT do ResponseDTO
            String token = responseDTO.getToken(); // Aqui pegamos o token
            System.out.println("Login realizado com sucesso: " + body.email());
            System.out.println("Token gerado: " + token);

            // Criação do cookie com o token JWT
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true); // Torna o cookie inacessível via JavaScript
            cookie.setSecure(false);   // Define o cookie como seguro (se estiver em HTTPS)
            cookie.setPath("/");      // Define o caminho para o qual o cookie será enviado
            cookie.setMaxAge(3600);   // Define a expiração do cookie (1 hora)
            response.addCookie(cookie); // Adiciona o cookie à resposta

            return ResponseEntity.ok(responseDTO); // Retorna o ResponseDTO com a mensagem e o token

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Exibe o erro completo no console
            return ResponseEntity.status(500).body("Erro ao realizar login.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO body) {
        try {
            // Converte RegisterRequestDTO para UsuarioDTO
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNome(body.nome());
            usuarioDTO.setEmail(body.email());
            usuarioDTO.setCpf(body.cpf());
            usuarioDTO.setSenha(body.senha());

            ResponseDTO response = usuarioService.register(usuarioDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao registrar usuário.");
        }
    }

    // Esqueci minha senha (gera e envia token)
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("E-mail é obrigatório.");
        }

        String token = usuarioService.generateAndSendPasswordResetToken(email);

        if (token == null) {
            // Retorna mensagem genérica para não expor a existência ou não do e-mail no sistema
            return ResponseEntity.ok("Se o e-mail existir em nossa base, as instruções de recuperação foram enviadas.");
        }

        // Opcional: Retornar o token para desenvolvimento ou debug
        return ResponseEntity.ok(Map.of(
                "message", "Se o e-mail existir em nossa base, as instruções de recuperação foram enviadas.",
                "token", token
        ));
    }

    // Redefinir senha
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newPassword = payload.get("newPassword");

        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body("A nova senha é obrigatória.");
        }

        boolean resetSuccess = usuarioService.resetPasswordWithToken(token, newPassword);
        if (resetSuccess) {
            return ResponseEntity.ok("Senha redefinida com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
    }
}