//package com.duckbill.cine_list.controller;
//
//import com.duckbill.cine_list.dto.UsuarioDTO;
//import com.duckbill.cine_list.service.UsuarioService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/usuarios")
//public class UsuarioController {
//
//    @Autowired
//    private UsuarioService usuarioService;
//
//    // Endpoint para criar um novo usuário
//    @PostMapping
//    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
//        try {
//            UsuarioDTO createdUsuario = usuarioService.create(usuarioDTO);
//            return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
//
//    // Endpoint para buscar um usuário por ID
//    @GetMapping("/{id}")
//    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable UUID id) {
//        Optional<UsuarioDTO> usuario = usuarioService.getById(id);
//        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Endpoint para listar todos os usuários
//    @GetMapping
//    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
//        List<UsuarioDTO> usuarios = usuarioService.getAll();
//        return ResponseEntity.ok(usuarios);
//    }
//
//    // Endpoint para atualizar um usuário por ID
//    @PutMapping("/{id}")
//    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable UUID id, @RequestBody UsuarioDTO usuarioDTO) {
//        try {
//            UsuarioDTO updatedUsuario = usuarioService.update(id, usuarioDTO);
//            return ResponseEntity.ok(updatedUsuario);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // Endpoint para excluir logicamente um usuário por ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUsuario(@PathVariable UUID id) {
//        usuarioService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//}