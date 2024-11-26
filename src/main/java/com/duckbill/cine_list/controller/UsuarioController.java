package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.dto.ResponseDTO;
import com.duckbill.cine_list.dto.UsuarioDTO;
import com.duckbill.cine_list.infra.security.TokenService;
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

    @Autowired
    private TokenService tokenService;

    // Endpoint para criar um novo usuário
    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO createdUsuario = usuarioService.create(usuarioDTO);
            return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
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
        try {
            // Atualiza o usuário e gera um novo token
            ResponseDTO response = usuarioService.update(id, usuarioDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
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
}