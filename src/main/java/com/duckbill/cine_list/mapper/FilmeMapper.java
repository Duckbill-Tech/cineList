package com.duckbill.cine_list.mapper;

import com.duckbill.cine_list.db.entity.Filme;
import com.duckbill.cine_list.dto.FilmeDTO;

public class FilmeMapper {

    public static FilmeDTO toDto(Filme filme) {
        return new FilmeDTO(
                filme.getId(),
                filme.getTitulo(),
                filme.getNota(),
                filme.getUpdatedAt(),
                filme.getCompletedAt(),
                filme.getDeletedAt()
        );
    }

    public static Filme toEntity(FilmeDTO filmeDTO) {
        Filme filme = new Filme();
        filme.setId(filmeDTO.getId());
        filme.setTitulo(filmeDTO.getTitulo());
        filme.setNota(filmeDTO.getNota());
        filme.setUpdatedAt(filmeDTO.getUpdatedAt());
        filme.setCompletedAt(filmeDTO.getCompletedAt());
        filme.setDeletedAt(filmeDTO.getDeletedAt());
        return filme;
    }
}