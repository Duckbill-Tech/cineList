package com.duckbill.cine_list.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class UsuarioDTO {

    private String id;

    @Email(message = "Email deve ser válido.")
    @NotEmpty(message = "Email não pode ser vazio.")
    private String email;

    @NotEmpty(message = "Nome não pode ser vazio.")
    private String nome;

    @NotEmpty(message = "CPF não pode ser vazio.")
    private String cpf;

    @NotEmpty(message = "Senha não pode ser vazia.")
    private String senha; // Adicionado campo senha

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // Construtores, Getters e Setters
    public UsuarioDTO() {
    }

    public UsuarioDTO(String id, String nome, String email, String cpf, String senha, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {  // Adiciona getter de senha
        return senha;
    }

    public void setSenha(String senha) {  // Adiciona setter de senha
        this.senha = senha;
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
}