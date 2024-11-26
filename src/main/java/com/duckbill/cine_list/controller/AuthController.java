package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.dto.*;
import com.duckbill.cine_list.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    // Endpoint para Login de usuário
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body, HttpServletResponse response) {
        try {
            LoginResponseDTO responseDTO = usuarioService.login(body.email(), body.senha());

            String token = responseDTO.getToken();

            // Criação do cookie com o token JWT
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true); // Torna o cookie inacessível via JavaScript!!!
            cookie.setSecure(false);   // Define o cookie como seguro (se estiver em HTTPS)
            cookie.setPath("/");      // Define o caminho para o qual o cookie será enviado
            cookie.setMaxAge(3600);   // Define a expiração do cookie (1 hora)
            response.addCookie(cookie); // Adiciona o cookie à resposta

            return ResponseEntity.ok(responseDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao realizar login.");
        }
    }

    // Endpoint para registrar de usuário (token)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO body, HttpServletResponse response) {
        try {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setNome(body.nome());
            usuarioDTO.setEmail(body.email());
            usuarioDTO.setCpf(body.cpf());
            usuarioDTO.setSenha(body.senha());

            ResponseDTO responseDTO = usuarioService.register(usuarioDTO);
            String token = responseDTO.getToken();

            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao registrar usuário.");
        }
    }
}