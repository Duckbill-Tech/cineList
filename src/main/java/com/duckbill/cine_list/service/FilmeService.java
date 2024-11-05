package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Filme;
import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.FilmeRepository;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.FilmeDTO;
import com.duckbill.cine_list.mapper.FilmeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class FilmeService {

    @Autowired
    private FilmeRepository filmeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FilmeMapper filmeMapper;

    // Metodo para criar um novo filme com data de criação e atualização
    public FilmeDTO create(FilmeDTO filmeDTO) {
        Usuario createdBy = null;

        if (filmeDTO.getCreatedById() != null) {
            createdBy = usuarioRepository.findById(UUID.fromString(filmeDTO.getCreatedById()))
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        }

        Filme filme = filmeMapper.toEntity(filmeDTO, createdBy);

        if (filme.getId() == null) {
            filme.setId(UUID.randomUUID().toString());
        }

        Filme savedFilme = filmeRepository.save(filme);
        return filmeMapper.toDto(savedFilme);
    }

    /* Metodo para buscar um filme pelo ID, garantindo que não esteja deletado
     Quando um filme é marcado como deletado (com deletedAt preenchido), a intenção é que ele
     não esteja mais acessível aos usuários ou ao sistema, mesmo que ainda exista fisicamente no banco de dados"*/
    public Optional<FilmeDTO> getById(UUID id) {
        return filmeRepository.findById(id)
                .filter(filme -> filme.getDeletedAt() == null)
                .map(filmeMapper::toDto);
    }

    // Metodo para listar todos os filmes que não estão deletados
    public List<FilmeDTO> getAll() {
        return filmeRepository.findAll()
                .stream()
                .filter(filme -> filme.getDeletedAt() == null)
                .map(filmeMapper::toDto)
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
                    return filmeMapper.toDto(updatedFilme);
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