package com.duckbill.cine_list.db.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "filme")
public class Filme {

    @Id // Define que este atributo é a chave primária da tabela
    @GeneratedValue
    private UUID id;

    @Column(nullable = false) // Define que o campo não pode ser nulo no banco de dados
    private String titulo;

    @Column // Define o usuário que criou o filme (chave estrangeira)
    private UUID createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Define a data de criação para o momento atual

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(); // Define a data de atualização para o momento atual

    @Column
    private LocalDateTime completedAt; // Data em que o filme foi marcado como completado (assistido)

    @Column
    private LocalDateTime deletedAt; // Data de exclusão lógica

    @Column(precision = 2, scale = 1)
    private Double nota; // Avaliação do filme

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

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}
