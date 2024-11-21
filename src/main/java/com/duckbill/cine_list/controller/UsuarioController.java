package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.dto.ResponseDTO;
import com.duckbill.cine_list.dto.UsuarioDTO;
import com.duckbill.cine_list.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para criar um novo usuário
    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            System.out.println("Recebendo usuarioDTO: " + usuarioDTO); // Log para verificar o objeto recebido
            UsuarioDTO createdUsuario = usuarioService.create(usuarioDTO);
            System.out.println("Usuario criado com sucesso: " + createdUsuario); // Log após criação
            return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage()); // Log para erros de validação
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage()); // Log para outros erros
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint para buscar um usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable UUID id) {
        Optional<UsuarioDTO> usuario = usuarioService.getById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getAll();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para atualizar um usuário por ID
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateUsuario(@PathVariable UUID id, @RequestBody UsuarioDTO usuarioDTO) {
        System.err.println("Iniciando atualização de usuário com ID: " + id);  // Loga o ID recebido

        // Loga os detalhes do payload recebido
        System.err.println("Payload recebido para atualização: Nome = " + usuarioDTO.getNome() + ", Email = " + usuarioDTO.getEmail() + ", CPF = " + usuarioDTO.getCpf());

        try {
            // Atualiza o usuário e gera um novo token
            ResponseDTO response = usuarioService.update(id, usuarioDTO);

            // Loga os detalhes do usuário atualizado e o novo token
            System.err.println("Usuário atualizado com sucesso: Nome = " + response.nome() + ", Novo Token = " + response.token());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());  // Loga a exceção caso ocorra
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar usuário: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // Endpoint para excluir logicamente um usuário por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable UUID id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint adicional para retornar uma mensagem de sucesso
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("sucesso!");
    }

    // Endpoint para resetar a senha
    @PostMapping("/reset-password")
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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("E-mail é obrigatório.");
        }

        try {
            usuarioService.generateAndSendPasswordResetToken(email);
            return ResponseEntity.ok("Instruções de recuperação de senha enviadas.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}