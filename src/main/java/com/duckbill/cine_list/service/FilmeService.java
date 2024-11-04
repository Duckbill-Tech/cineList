package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Filme;
import com.duckbill.cine_list.db.repository.FilmeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class FilmeService {

    @Autowired
    private FilmeRepository filmeRepository;

    // Metodo para criar um novo filme com data de criação e atualização
    public Filme create(Filme filme) {
        filme.setCreatedAt(LocalDateTime.now());
        return filmeRepository.save(filme);
    }

    @PostConstruct
    public Filme generateRandomFilme() {
        Filme randomFilme = new Filme();

        randomFilme.setTitulo("Filme " + new Random().nextInt(1000));

        return create(randomFilme);
    }
    /* Metodo para buscar um filme pelo ID, garantindo que não esteja deletado
     Quando um filme é marcado como deletado (com deletedAt preenchido), a intenção é que ele
     não esteja mais acessível aos usuários ou ao sistema, mesmo que ainda exista fisicamente no banco de dados"*/
    public Optional<Filme> getById(UUID id) {
        return filmeRepository.findById(id)
                .filter(filme -> filme.getDeletedAt() == null);
    }

    // Metodo para listar todos os filmes que não estão deletados
    public List<Filme> getAll() {
        return filmeRepository.findAll()
                .stream()
                .filter(filme -> filme.getDeletedAt() == null)
                .toList();
    }

    // Metodo para atualizar um filme existente
    public Filme update(UUID id, Filme filmeDetails) {
        return filmeRepository.findById(id)
                .map(filme -> {
                    filme.setTitulo(filmeDetails.getTitulo());
                    filme.setNota(filmeDetails.getNota());
                    filme.setUpdatedAt(LocalDateTime.now());
                    return filmeRepository.save(filme);
                })
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));
    }

    // Metodo para deletar logicamente um filme
    public void delete(UUID id) {
        filmeRepository.findById(id)
                .filter(filme -> filme.getDeletedAt() == null)
                .ifPresent(filme -> {
                    filme.setDeletedAt(LocalDateTime.now());
                    filmeRepository.save(filme);
                });
    }
}