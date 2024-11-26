package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.dto.FilmeDTO;
import com.duckbill.cine_list.infra.security.TokenService;
import com.duckbill.cine_list.service.FilmeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmeControllerTest {

    @InjectMocks
    private FilmeController filmeController;

    @Mock
    private FilmeService filmeService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private TokenService tokenService;

    private FilmeDTO filmeDTO;
    private UUID filmeId;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        filmeId = UUID.randomUUID();
        usuarioId = UUID.randomUUID();
        filmeDTO = new FilmeDTO();
        filmeDTO.setId(filmeId);
        filmeDTO.setTitulo("Example Title");
    }

    // Teste para criação de um filme com sucesso
    @Test
    void testCreateFilme() {
        // Mock do cookie para autenticação
        Cookie authCookie = new Cookie("authToken", "mocked-token");
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[]{authCookie});

        // Mock do FilmeService para retornar o filme esperado
        when(filmeService.create(eq(filmeDTO), eq(usuarioId))).thenReturn(filmeDTO);

        // Simula o método privado getUsuarioIdFromToken
        FilmeController filmeControllerSpy = spy(filmeController);
        doReturn(usuarioId).when(filmeControllerSpy).getUsuarioIdFromToken(httpServletRequest);

        // Execução do método no controller
        ResponseEntity<FilmeDTO> response = filmeControllerSpy.createFilme(filmeDTO, httpServletRequest);

        // Verificações
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(filmeDTO, response.getBody());
        verify(filmeService, times(1)).create(eq(filmeDTO), eq(usuarioId));
    }


    // Teste para buscar filme por ID válido
    @Test
    void testGetFilmeById_ValidId() {
        when(filmeService.getById(filmeId)).thenReturn(Optional.of(filmeDTO));

        ResponseEntity<FilmeDTO> response = filmeController.getFilmeById(filmeId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filmeDTO, response.getBody());
        verify(filmeService, times(1)).getById(filmeId);
    }

    // Teste para erro de formato de ID inválido
    @Test
    void testGetFilmeById_InvalidId() {
        ResponseEntity<FilmeDTO> response = filmeController.getFilmeById("invalid-uuid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Teste para filme não encontrado
    @Test
    void testGetFilmeById_NotFound() {
        when(filmeService.getById(filmeId)).thenReturn(Optional.empty());

        ResponseEntity<FilmeDTO> response = filmeController.getFilmeById(filmeId.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(filmeService, times(1)).getById(filmeId);
    }

    // Teste para listar todos os filmes
    @Test
    void testGetAllFilmes() {
        List<FilmeDTO> filmes = new ArrayList<>();
        filmes.add(filmeDTO);
        when(filmeService.getAll()).thenReturn(filmes);

        ResponseEntity<List<FilmeDTO>> response = filmeController.getAllFilmes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filmes, response.getBody());
        verify(filmeService, times(1)).getAll();
    }

    // Teste para atualizar filme por ID válido
    @Test
    void testUpdateFilme_ValidId() {
        when(filmeService.update(eq(filmeId), any(FilmeDTO.class))).thenReturn(filmeDTO);

        ResponseEntity<FilmeDTO> response = filmeController.updateFilme(filmeId.toString(), filmeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filmeDTO, response.getBody());
        verify(filmeService, times(1)).update(eq(filmeId), any(FilmeDTO.class));
    }

    // Teste para erro de formato de ID inválido na atualização
    @Test
    void testUpdateFilme_InvalidId() {
        ResponseEntity<FilmeDTO> response = filmeController.updateFilme("invalid-uuid", filmeDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Teste para erro de filme não encontrado na atualização
    @Test
    void testUpdateFilme_NotFound() {
        when(filmeService.update(eq(filmeId), any(FilmeDTO.class))).thenThrow(new RuntimeException());

        ResponseEntity<FilmeDTO> response = filmeController.updateFilme(filmeId.toString(), filmeDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(filmeService, times(1)).update(eq(filmeId), any(FilmeDTO.class));
    }

    // Teste para exclusão de filme com ID válido
    @Test
    void testDeleteFilme_ValidId() {
        ResponseEntity<Void> response = filmeController.deleteFilme(filmeId.toString());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(filmeService, times(1)).delete(filmeId);
    }

    // Teste para erro de formato de ID inválido na exclusão
    @Test
    void testDeleteFilme_InvalidId() {
        ResponseEntity<Void> response = filmeController.deleteFilme("invalid-uuid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
