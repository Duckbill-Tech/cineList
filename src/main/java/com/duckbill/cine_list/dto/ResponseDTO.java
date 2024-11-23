package com.duckbill.cine_list.dto;

public class ResponseDTO {
    private String message;
    private String token;
    
    public ResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // Getters e setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}