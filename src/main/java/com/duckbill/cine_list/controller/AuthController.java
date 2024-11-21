package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.dto.*;
import com.duckbill.cine_list.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {
        try {
            ResponseDTO response = usuarioService.login(body.email(), body.senha());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
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
    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("E-mail é obrigatório.");
        }

        boolean emailSent = usuarioService.generateAndSendPasswordResetToken(email);
        return ResponseEntity.ok("Se o e-mail existir em nossa base, as instruções de recuperação foram enviadas.");
    }

    // Redefinir senha
    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
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