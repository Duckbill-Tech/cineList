package com.duckbill.cine_list.dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class FilmeDTO {

    private String id;

    @NotEmpty(message = "Titulo não pode ser vazio.")
    private String titulo;

    private Double nota;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private LocalDateTime deletedAt;
    private String createdById;

    // Construtor padrão
    public FilmeDTO() {
    }

    // Construtor com todos os parâmetros
    public FilmeDTO(String id, String titulo, Double nota, LocalDateTime updatedAt, LocalDateTime completedAt, LocalDateTime deletedAt, String createdById) {
        this.id = id;
        this.titulo = titulo;
        this.nota = nota;
        this.updatedAt = updatedAt;
        this.completedAt = completedAt;
        this.deletedAt = deletedAt;
        this.createdById = createdById;
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
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

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }
}