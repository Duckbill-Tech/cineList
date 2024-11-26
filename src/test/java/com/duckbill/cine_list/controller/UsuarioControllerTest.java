package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.dto.ResponseDTO;
import com.duckbill.cine_list.dto.UsuarioDTO;
import com.duckbill.cine_list.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID usuarioId = UUID.randomUUID();
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuarioId);
        usuarioDTO.setNome("Teste Usuario");
        usuarioDTO.setEmail("teste@usuario.com");
        usuarioDTO.setCpf("123.456.789-00");
    }

    @Test
    void testCreateUsuario() {
        when(usuarioService.create(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.createUsuario(usuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(usuarioDTO.getNome(), response.getBody().getNome());
        verify(usuarioService, times(1)).create(any(UsuarioDTO.class));
    }

    @Test
    void testCreateUsuarioBadRequest() {
        when(usuarioService.create(any(UsuarioDTO.class))).thenThrow(new IllegalArgumentException("CPF inválido"));

        ResponseEntity<UsuarioDTO> response = usuarioController.createUsuario(usuarioDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioService, times(1)).create(any(UsuarioDTO.class));
    }

    @Test
    void testCreateUsuarioInternalServerError() {
        when(usuarioService.create(any(UsuarioDTO.class))).thenThrow(new RuntimeException("Erro no banco de dados"));

        ResponseEntity<UsuarioDTO> response = usuarioController.createUsuario(usuarioDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(usuarioService, times(1)).create(any(UsuarioDTO.class));
    }

    @Test
    void testGetUsuarioById() {
        UUID id = usuarioDTO.getId();
        when(usuarioService.getById(id)).thenReturn(Optional.of(usuarioDTO));

        ResponseEntity<UsuarioDTO> response = usuarioController.getUsuarioById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(usuarioDTO.getNome(), response.getBody().getNome());
        verify(usuarioService, times(1)).getById(id);
    }

    @Test
    void testGetUsuarioByIdNotFound() {
        UUID id = usuarioDTO.getId();
        when(usuarioService.getById(id)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioDTO> response = usuarioController.getUsuarioById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuarioService, times(1)).getById(id);
    }

    @Test
    void testGetAllUsuarios() {
        List<UsuarioDTO> usuarios = new ArrayList<>();
        usuarios.add(usuarioDTO);
        when(usuarioService.getAll()).thenReturn(usuarios);

        ResponseEntity<List<UsuarioDTO>> response = usuarioController.getAllUsuarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(usuarioService, times(1)).getAll();
    }

    @Test
    void testUpdateUsuario() {
        UUID id = usuarioDTO.getId();
        ResponseDTO updatedResponseDTO = new ResponseDTO("Updated Usuario", "new-jwt-token");

        when(usuarioService.update(eq(id), any(UsuarioDTO.class))).thenReturn(updatedResponseDTO);

        ResponseEntity<ResponseDTO> response = usuarioController.updateUsuario(id, usuarioDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedResponseDTO.getMessage(), response.getBody().getMessage());
        assertEquals(updatedResponseDTO.getToken(), response.getBody().getToken());
        verify(usuarioService, times(1)).update(eq(id), any(UsuarioDTO.class));
    }

    @Test
    void testUpdateUsuarioNotFound() {
        UUID id = usuarioDTO.getId();
        when(usuarioService.update(eq(id), any(UsuarioDTO.class))).thenThrow(new RuntimeException("Usuário não encontrado"));

        ResponseEntity<ResponseDTO> response = usuarioController.updateUsuario(id, usuarioDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuarioService, times(1)).update(eq(id), any(UsuarioDTO.class));
    }

    @Test
    void testDeleteUsuario() {
        UUID id = usuarioDTO.getId();

        ResponseEntity<Void> response = usuarioController.deleteUsuario(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioService, times(1)).delete(id);
    }

    @Test
    void testGetUserWithRoleUser() {
        ResponseEntity<String> response = usuarioController.getUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("sucesso!", response.getBody());
    }
}
