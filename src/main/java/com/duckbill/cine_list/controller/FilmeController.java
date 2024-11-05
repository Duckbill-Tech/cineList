//package com.duckbill.cine_list.controller;
//
//import com.duckbill.cine_list.dto.FilmeDTO;
//import com.duckbill.cine_list.service.FilmeService;
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
//@RequestMapping("/api/filmes")
//public class FilmeController {
//
//    @Autowired
//    private FilmeService filmeService;
//
//    // Endpoint para criar um novo filme
//    @PostMapping
//    public ResponseEntity<FilmeDTO> createFilme(@RequestBody FilmeDTO filmeDTO) {
//        FilmeDTO createdFilme = filmeService.create(filmeDTO);
//        return new ResponseEntity<>(createdFilme, HttpStatus.CREATED);
//    }
//
//    // Endpoint para buscar um filme por ID
//    @GetMapping("/{id}")
//    public ResponseEntity<FilmeDTO> getFilmeById(@PathVariable UUID id) {
//        Optional<FilmeDTO> filme = filmeService.getById(id);
//        return filme.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // Endpoint para listar todos os filmes
//    @GetMapping
//    public ResponseEntity<List<FilmeDTO>> getAllFilmes() {
//        List<FilmeDTO> filmes = filmeService.getAll();
//        return ResponseEntity.ok(filmes);
//    }
//
//    // Endpoint para atualizar um filme por ID
//    @PutMapping("/{id}")
//    public ResponseEntity<FilmeDTO> updateFilme(@PathVariable UUID id, @RequestBody FilmeDTO filmeDTO) {
//        try {
//            FilmeDTO updatedFilme = filmeService.update(id, filmeDTO);
//            return ResponseEntity.ok(updatedFilme);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // Endpoint para excluir logicamente um filme por ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteFilme(@PathVariable UUID id) {
//        filmeService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//}