package com.duckbill.cine_list.dto;

public class LoginResponseDTO {
    private String nomeUsuario;
    private String emailUsuario;
    private String token;

    public LoginResponseDTO(String nomeUsuario, String emailUsuario, String token) {
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.token = token;
    }
    
    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}