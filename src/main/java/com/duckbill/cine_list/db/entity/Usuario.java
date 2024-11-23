package com.duckbill.cine_list.db.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String cpf;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Filme> filmesCriados;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "token_expiration_time")
    private LocalDateTime tokenExpirationTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Filme> getFilmesCriados() {
        return filmesCriados;
    }

    public void setFilmesCriados(List<Filme> filmesCriados) {
        this.filmesCriados = filmesCriados;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    // TODO: Esqueci minha senha
    public String getPasswordResetToken() { return passwordResetToken; }

    public void setPasswordResetToken(String passwordResetToken) { this.passwordResetToken = passwordResetToken; }

    public LocalDateTime getTokenExpirationTime() { return tokenExpirationTime; }

    public void setTokenExpirationTime(LocalDateTime tokenExpirationTime) { this.tokenExpirationTime = tokenExpirationTime; }
}