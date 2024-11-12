package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.LoginRequestDTO;
import com.duckbill.cine_list.dto.RegisterRequestDTO;
import com.duckbill.cine_list.dto.ResponseDTO;
import com.duckbill.cine_list.infra.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {
        Usuario usuario = usuarioRepository.findByEmail(body.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        // Caso a senha der match
        if (passwordEncoder.matches(body.password(), usuario.getSenha())) {
            String token = tokenService.generateToken(usuario);
            return ResponseEntity.ok(new ResponseDTO(usuario.getNome(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO body) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(body.email());

        if (usuarioExistente.isEmpty()) {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setSenha(passwordEncoder.encode(body.password()));
            novoUsuario.setEmail(body.email());
            novoUsuario.setNome(body.name());
            usuarioRepository.save(novoUsuario);

            String token = tokenService.generateToken(novoUsuario);
            return ResponseEntity.ok(new ResponseDTO(novoUsuario.getNome(), token));
        }

        return ResponseEntity.badRequest().build();
    }
}