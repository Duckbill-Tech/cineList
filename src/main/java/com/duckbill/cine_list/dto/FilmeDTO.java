package com.duckbill.cine_list.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

public class FilmeDTO {

    private UUID id;

    @NotEmpty(message = "Titulo não pode ser vazio.")
    private String titulo;

    private String descricao;

    private Double nota;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private LocalDateTime deletedAt;
    private UUID createdById;


    public FilmeDTO() {
    }


    public FilmeDTO(UUID id, String titulo, String descricao, Double nota, LocalDateTime updatedAt, LocalDateTime completedAt, LocalDateTime deletedAt, UUID createdById) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.nota = nota;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
        this.deletedAt = deletedAt;
        this.createdById = createdById;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public UUID getCreatedById() {
        return createdById;
    }

    public void setCreatedById(UUID createdById) {
        this.createdById = createdById;
    }
}