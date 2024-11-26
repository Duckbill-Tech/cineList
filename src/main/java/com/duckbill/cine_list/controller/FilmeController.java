package com.duckbill.cine_list.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.duckbill.cine_list.dto.FilmeDTO;
import com.duckbill.cine_list.service.FilmeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/filmes")
public class FilmeController {

    @Autowired
    private FilmeService filmeService;

    UUID getUsuarioIdFromToken(HttpServletRequest request) {
        // Busca o token no cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    try {
                        // Decodifica o token e extrai o ID do usuário
                        DecodedJWT decodedJWT = JWT.decode(cookie.getValue());
                        return UUID.fromString(decodedJWT.getClaim("userId").asString());
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // Endpoint para criar um novo filme
    @PostMapping
    public ResponseEntity<FilmeDTO> createFilme(@RequestBody FilmeDTO filmeDTO,  HttpServletRequest request) {
        UUID usuarioId = getUsuarioIdFromToken(request);
        FilmeDTO createdFilme = filmeService.create(filmeDTO, usuarioId);
        return new ResponseEntity<>(createdFilme, HttpStatus.CREATED);
    }

    // Endpoint para buscar um filme por ID
    @GetMapping("/{id}")
    public ResponseEntity<FilmeDTO> getFilmeById(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<FilmeDTO> filme = filmeService.getById(uuid);
            return filme.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint para listar todos os filmes
    @GetMapping
    public ResponseEntity<List<FilmeDTO>> getAllFilmes() {
        List<FilmeDTO> filmes = filmeService.getAll();
        return ResponseEntity.ok(filmes);
    }

    // Endpoint para atualizar um filme por ID
    @PutMapping("/{id}")
    public ResponseEntity<FilmeDTO> updateFilme(@PathVariable String id, @RequestBody FilmeDTO filmeDTO) {
        try {
            UUID uuid = UUID.fromString(id);
            FilmeDTO updatedFilme = filmeService.update(uuid, filmeDTO);
            return ResponseEntity.ok(updatedFilme);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para excluir logicamente um filme por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilme(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            filmeService.delete(uuid);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}