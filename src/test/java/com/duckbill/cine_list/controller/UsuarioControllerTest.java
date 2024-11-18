package com.duckbill.cine_list.controller;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

    // Testa a criação de um novo usuário
    @Test
    void testCreateUsuario() {
        when(usuarioService.create(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.createUsuario(usuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(usuarioDTO.getNome(), response.getBody().getNome());
        verify(usuarioService, times(1)).create(any(UsuarioDTO.class));
    }

    // Testa a criação de um usuário com *ERRO de validação*
    @Test
    void testCreateUsuarioBadRequest() {
        when(usuarioService.create(any(UsuarioDTO.class))).thenThrow(new IllegalArgumentException("CPF inválido"));

        ResponseEntity<UsuarioDTO> response = usuarioController.createUsuario(usuarioDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioService, times(1)).create(any(UsuarioDTO.class));
    }

    // Testa a criação de um usuário com *ERRO inesperado*
    @Test
    void testCreateUsuarioInternalServerError() {
        when(usuarioService.create(any(UsuarioDTO.class))).thenThrow(new RuntimeException("Erro no banco de dados"));

        ResponseEntity<UsuarioDTO> response = usuarioController.createUsuario(usuarioDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(usuarioService, times(1)).create(any(UsuarioDTO.class));
    }

    // Testa a obtenção de um usuário existente por ID
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

    // Testa a obtenção de um usuário com ID não encontrado
    @Test
    void testGetUsuarioByIdNotFound() {
        UUID id = usuarioDTO.getId();
        when(usuarioService.getById(id)).thenReturn(Optional.empty());

        ResponseEntity<UsuarioDTO> response = usuarioController.getUsuarioById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuarioService, times(1)).getById(id);
    }

    // Testa a obtenção de todos os usuários
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

    // Testa a atualização de um usuário existente
    @Test
    void testUpdateUsuario() {
        UUID id = usuarioDTO.getId();
        UsuarioDTO updatedUsuarioDTO = new UsuarioDTO();
        updatedUsuarioDTO.setId(id);
        updatedUsuarioDTO.setNome("Updated Usuario");
        updatedUsuarioDTO.setEmail("updated@usuario.com");
        updatedUsuarioDTO.setCpf("987.654.321-00");

        when(usuarioService.update(eq(id), any(UsuarioDTO.class))).thenReturn(updatedUsuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.updateUsuario(id, updatedUsuarioDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedUsuarioDTO.getNome(), response.getBody().getNome());
        verify(usuarioService, times(1)).update(eq(id), any(UsuarioDTO.class));
    }

    // Testa a atualização de um usuário com ID não encontrado
    @Test
    void testUpdateUsuarioNotFound() {
        UUID id = usuarioDTO.getId();
        when(usuarioService.update(eq(id), any(UsuarioDTO.class))).thenThrow(new RuntimeException("Usuário não encontrado"));

        ResponseEntity<UsuarioDTO> response = usuarioController.updateUsuario(id, usuarioDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuarioService, times(1)).update(eq(id), any(UsuarioDTO.class));
    }

    // Testa a exclusão de um usuário por ID
    @Test
    void testDeleteUsuario() {
        UUID id = usuarioDTO.getId();

        ResponseEntity<Void> response = usuarioController.deleteUsuario(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioService, times(1)).delete(id);
    }
}