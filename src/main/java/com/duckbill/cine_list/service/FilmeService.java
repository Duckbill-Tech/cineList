package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Filme;
import com.duckbill.cine_list.db.repository.FilmeRepository;
import com.duckbill.cine_list.dto.FilmeDTO;
import com.duckbill.cine_list.mapper.FilmeMapper;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;



@Service
public class FilmeService {

    @Autowired
    private FilmeRepository filmeRepository;

    // Metodo para criar um novo filme com data de criação e atualização
    public FilmeDTO create(FilmeDTO filmeDTO) {
        Filme filme = FilmeMapper.toEntity(filmeDTO); // Converte DTO para entidade
        if (filme.getId() == null) {
            filme.setId(UUID.randomUUID().toString());
        }
        Filme savedFilme = filmeRepository.save(filme);
        return FilmeMapper.toDto(savedFilme); // Converte entidade salva para DTO
    }

    /* Metodo para buscar um filme pelo ID, garantindo que não esteja deletado
     Quando um filme é marcado como deletado (com deletedAt preenchido), a intenção é que ele
     não esteja mais acessível aos usuários ou ao sistema, mesmo que ainda exista fisicamente no banco de dados"*/
    public Optional<FilmeDTO> getById(UUID id) {
        return filmeRepository.findById(id)
                .filter(filme -> filme.getDeletedAt() == null)
                .map(FilmeMapper::toDto); // Converte entidade para DTO
    }

    // Metodo para listar todos os filmes que não estão deletados
    public List<FilmeDTO> getAll() {
        return filmeRepository.findAll()
                .stream()
                .filter(filme -> filme.getDeletedAt() == null)
                .map(FilmeMapper::toDto)
                .collect(Collectors.toList());
    }

    // Metodo para atualizar um filme existente
    public FilmeDTO update(UUID id, FilmeDTO filmeDTO) {
        return filmeRepository.findById(id)
                .map(filme -> {
                    filme.setTitulo(filmeDTO.getTitulo());
                    filme.setNota(filmeDTO.getNota());
                    filme.setUpdatedAt(LocalDateTime.now());
                    Filme updatedFilme = filmeRepository.save(filme);
                    return FilmeMapper.toDto(updatedFilme);
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