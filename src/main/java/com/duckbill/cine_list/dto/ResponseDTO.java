package com.duckbill.cine_list.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDTO {
    // Getters e Setters
    private String message;
    private String token;

    public ResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

}