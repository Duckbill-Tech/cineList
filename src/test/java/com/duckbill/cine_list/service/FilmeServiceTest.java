package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Filme;
import com.duckbill.cine_list.db.repository.FilmeRepository;
import com.duckbill.cine_list.dto.FilmeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FilmeServiceTest {


    @Mock
    private FilmeRepository filmeRepository;

    @InjectMocks
    private FilmeService filmeService;

    private Filme filme;
    private FilmeDTO filmeDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        filme = new Filme();
        filme.setId(UUID.randomUUID().toString());
        filme.setTitulo("Test Filme");
        filme.setNota(5.0);
        filme.setUpdatedAt(LocalDateTime.now());

        filmeDTO = new FilmeDTO(filme.getId(), filme.getTitulo(), filme.getNota(), filme.getUpdatedAt(), filme.getCompletedAt(), filme.getDeletedAt());
    }

    /* O teste testCreate simula o comportamento do metodo save do filmeRepository,
     fazendo-o retornar o objeto filme sempre que é chamado com qualquer instância de Filme
     Chama o metodo create do filmeService e verifica se:
        - id do createdFilme não é nulo.
        - título do createdFilme é igual ao título do filme mockado.
        - metodo save foi chamado exatamente uma vez.*/
    @Test
    void testCreate() {
        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);

        FilmeDTO createdFilmeDTO = filmeService.create(filmeDTO);
        assertNotNull(createdFilmeDTO.getId());
        assertEquals(filmeDTO.getTitulo(), createdFilmeDTO.getTitulo());
        verify(filmeRepository, times(1)).save(any(Filme.class));
    }

    /* Testar getById com um filme válido e não deletado.
    Configura o mock para retornar filme quando findById for chamado com o id (se o filme é retornado e se o título
    do filme está correto).*/
    @Test
    void testGetByIdWithValidIdAndNotDeleted() {
        UUID id = UUID.fromString(filme.getId());
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        Optional<FilmeDTO> result = filmeService.getById(id);
        assertTrue(result.isPresent());
        assertEquals(filmeDTO.getTitulo(), result.get().getTitulo());
    }

    /* Verifica que um filme deletado não é retornado.*/
    @Test
    void testGetByIdWithDeletedFilme() {
        filme.setDeletedAt(LocalDateTime.now());
        UUID id = UUID.fromString(filme.getId());
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        Optional<FilmeDTO> result = filmeService.getById(id);
        assertFalse(result.isPresent());
    }

    /* Todos os filmes retornados não estão deletados.*/
    @Test
    void testGetAll() {
        Filme filme2 = new Filme();
        filme2.setId(UUID.randomUUID().toString());
        filme2.setTitulo("Another Test Filme");
        filme2.setNota(8.0);

        when(filmeRepository.findAll()).thenReturn(List.of(filme, filme2));

        List<FilmeDTO> filmes = filmeService.getAll();
        assertEquals(2, filmes.size());
        assertTrue(filmes.stream().allMatch(f -> f.getDeletedAt() == null));
    }

    /*Testa update, configurando o mock para que findById retorne o
    filme e save retorne o mesmo objeto após atualização.*/
    @Test
    void testUpdate() {
        UUID id = UUID.fromString(filme.getId());
        FilmeDTO updatedDetailsDTO = new FilmeDTO();
        updatedDetailsDTO.setTitulo("Updated Title");
        updatedDetailsDTO.setNota(9.0);

        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));
        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);

        FilmeDTO updatedFilmeDTO = filmeService.update(id, updatedDetailsDTO);
        assertEquals("Updated Title", updatedFilmeDTO.getTitulo());
        assertEquals(9, updatedFilmeDTO.getNota());
        assertNotNull(updatedFilmeDTO.getUpdatedAt());
    }

    /* Testa o cenário de atualização de um filme que não existe,
    configurando findById para retornar vazio e esperando uma exceção RuntimeException.*/
    @Test
    void testUpdateFilmeNotFound() {
        UUID id = UUID.randomUUID();
        when(filmeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> filmeService.update(id, new FilmeDTO()));
    }

    /* Testa delete, configurando o mock para que findById retorne o filme.*/
    @Test
    void testDelete() {
        UUID id = UUID.fromString(filme.getId());
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        filmeService.delete(id);
        assertNotNull(filme.getDeletedAt());
        verify(filmeRepository, times(1)).save(filme);
    }

    /* Testa delete em um filme já deletado, garantindo que save não é chamado..*/
    @Test
    void testDeleteAlreadyDeleted() {
        filme.setDeletedAt(LocalDateTime.now());
        UUID id = UUID.fromString(filme.getId());
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        filmeService.delete(id);
        verify(filmeRepository, never()).save(filme);
    }
}
