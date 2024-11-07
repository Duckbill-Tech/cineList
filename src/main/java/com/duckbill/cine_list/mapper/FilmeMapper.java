package com.duckbill.cine_list.mapper;

import com.duckbill.cine_list.db.entity.Filme;
import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.dto.FilmeDTO;
import org.springframework.stereotype.Component;

@Component
public class FilmeMapper {

    public FilmeDTO toDto(Filme filme) {
        return new FilmeDTO(
                filme.getId(),
                filme.getTitulo(),
                filme.getDescricao(), // Novo campo descricao
                filme.getNota(),
                filme.getUpdatedAt(),
                filme.getCompletedAt(),
                filme.getDeletedAt(),
                filme.getCreatedBy() != null ? filme.getCreatedBy().getId() : null
        );
    }

    public Filme toEntity(FilmeDTO filmeDTO, Usuario createdBy) {
        Filme filme = new Filme();
        filme.setId(filmeDTO.getId());
        filme.setTitulo(filmeDTO.getTitulo());
        filme.setDescricao(filmeDTO.getDescricao()); // Novo campo descricao
        filme.setNota(filmeDTO.getNota());
        filme.setUpdatedAt(filmeDTO.getUpdatedAt());
        filme.setCompletedAt(filmeDTO.getCompletedAt());
        filme.setDeletedAt(filmeDTO.getDeletedAt());
        filme.setCreatedBy(createdBy); // Associa o usuário criador
        return filme;
    }
}