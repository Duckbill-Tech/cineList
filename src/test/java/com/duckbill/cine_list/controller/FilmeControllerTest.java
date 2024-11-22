//package com.duckbill.cine_list.controller;
//
//import com.duckbill.cine_list.dto.FilmeDTO;
//import com.duckbill.cine_list.service.FilmeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class FilmeControllerTest {
//
//    @InjectMocks
//    private FilmeController filmeController;
//
//    @Mock
//    private FilmeService filmeService;
//
//    private FilmeDTO filmeDTO;
//    private UUID filmeId;
//    private UUID usuarioId;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        filmeId = UUID.randomUUID();
//        usuarioId = UUID.randomUUID();
//        filmeDTO = new FilmeDTO();
//        filmeDTO.setId(filmeId);
//        filmeDTO.setTitulo("Example Title");
//    }
//
//    // Verifica se o filme é criado com sucesso
//    @Test
//    void testCreateFilme() {
//        when(filmeService.create(any(FilmeDTO.class), eq(usuarioId))).thenReturn(filmeDTO);
//
//        ResponseEntity<FilmeDTO> response = filmeController.createFilme(filmeDTO, usuarioId);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(filmeDTO, response.getBody());
//        verify(filmeService, times(1)).create(any(FilmeDTO.class), eq(usuarioId));
//    }
//
//    // Verifica a resposta quando o ID fornecido é válido e o filme existe
//    @Test
//    void testGetFilmeById_ValidId() {
//        when(filmeService.getById(filmeId)).thenReturn(Optional.of(filmeDTO));
//
//        ResponseEntity<FilmeDTO> response = filmeController.getFilmeById(filmeId.toString());
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(filmeDTO, response.getBody());
//        verify(filmeService, times(1)).getById(filmeId);
//    }
//
//    // Verifica a resposta quando o ID fornecido é válido e o filme existe
//    @Test
//    void testGetFilmeById_InvalidId() {
//        ResponseEntity<FilmeDTO> response = filmeController.getFilmeById("invalid-uuid");
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
//
//    // Verifica a resposta quando o filme não é encontrado
//    @Test
//    void testGetFilmeById_NotFound() {
//        when(filmeService.getById(filmeId)).thenReturn(Optional.empty());
//
//        ResponseEntity<FilmeDTO> response = filmeController.getFilmeById(filmeId.toString());
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        verify(filmeService, times(1)).getById(filmeId);
//    }
//
//    // Verifica se a listagem de todos os filmes retorna corretamente
//    @Test
//    void testGetAllFilmes() {
//        List<FilmeDTO> filmes = new ArrayList<>();
//        filmes.add(filmeDTO);
//        when(filmeService.getAll()).thenReturn(filmes);
//
//        ResponseEntity<List<FilmeDTO>> response = filmeController.getAllFilmes();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(filmes, response.getBody());
//        verify(filmeService, times(1)).getAll();
//    }
//
//    // Verifica a atualização de um filme existente
//    @Test
//    void testUpdateFilme_ValidId() {
//        when(filmeService.update(eq(filmeId), any(FilmeDTO.class))).thenReturn(filmeDTO);
//
//        ResponseEntity<FilmeDTO> response = filmeController.updateFilme(filmeId.toString(), filmeDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(filmeDTO, response.getBody());
//        verify(filmeService, times(1)).update(eq(filmeId), any(FilmeDTO.class));
//    }
//
//    // Verifica o tratamento de um ID inválido na atualização
//    @Test
//    void testUpdateFilme_InvalidId() {
//        ResponseEntity<FilmeDTO> response = filmeController.updateFilme("invalid-uuid", filmeDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
//
//    // Verifica o tratamento de filme não encontrado na atualização
//    @Test
//    void testUpdateFilme_NotFound() {
//        when(filmeService.update(eq(filmeId), any(FilmeDTO.class))).thenThrow(new RuntimeException());
//
//        ResponseEntity<FilmeDTO> response = filmeController.updateFilme(filmeId.toString(), filmeDTO);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        verify(filmeService, times(1)).update(eq(filmeId), any(FilmeDTO.class));
//    }
//
//    // Verifica se o filme é excluído com sucesso
//    @Test
//    void testDeleteFilme_ValidId() {
//        ResponseEntity<Void> response = filmeController.deleteFilme(filmeId.toString());
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(filmeService, times(1)).delete(filmeId);
//    }
//
//    // Verifica o tratamento de um ID inválido na exclusão
//    @Test
//    void testDeleteFilme_InvalidId() {
//        ResponseEntity<Void> response = filmeController.deleteFilme("invalid-uuid");
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }
//}