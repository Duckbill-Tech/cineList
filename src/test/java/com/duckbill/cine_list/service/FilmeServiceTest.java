package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Filme;
import com.duckbill.cine_list.db.repository.FilmeRepository;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        filme = new Filme();
        filme.setId(UUID.randomUUID().toString());
        filme.setTitulo("Test Filme");
        filme.setNota(5.0);
        filme.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreate() {
        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);

        Filme createdFilme = filmeService.create(new Filme());
        assertNotNull(createdFilme.getId());
        assertEquals(filme.getTitulo(), createdFilme.getTitulo());
        verify(filmeRepository, times(1)).save(any(Filme.class));
    }

    @Test
    void testGenerateRandomFilme() {
        when(filmeRepository.save(any(Filme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Filme randomFilme = filmeService.generateRandomFilme();
        assertNotNull(randomFilme.getId());
        assertTrue(randomFilme.getTitulo().startsWith("Filme"), "O título do filme aleatório deve começar com 'Filme'");
        verify(filmeRepository, times(1)).save(any(Filme.class));
    }

    @Test
    void testGetByIdWithValidIdAndNotDeleted() {
        UUID id = UUID.fromString(filme.getId());
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        Optional<Filme> result = filmeService.getById(id);
        assertTrue(result.isPresent());
        assertEquals(filme.getTitulo(), result.get().getTitulo());
    }

    @Test
    void testGetByIdWithDeletedFilme() {
        filme.setDeletedAt(LocalDateTime.now());
        UUID id = UUID.fromString(filme.getId());
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        Optional<Filme> result = filmeService.getById(id);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAll() {
        Filme filme2 = new Filme();
        filme2.setId(UUID.randomUUID().toString());
        filme2.setTitulo("Another Test Filme");
        filme2.setNota(8.0);

        when(filmeRepository.findAll()).thenReturn(List.of(filme, filme2));

        List<Filme> filmes = filmeService.getAll();
        assertEquals(2, filmes.size());
        assertTrue(filmes.stream().allMatch(f -> f.getDeletedAt() == null));
    }

    @Test
    void testUpdate() {
        UUID id = UUID.fromString(filme.getId());
        Filme updatedDetails = new Filme();
        updatedDetails.setTitulo("Updated Title");
        updatedDetails.setNota(9.0);

        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));
        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);

        Filme updatedFilme = filmeService.update(id, updatedDetails);
        assertEquals("Updated Title", updatedFilme.getTitulo());
        assertEquals(9, updatedFilme.getNota());
        assertNotNull(updatedFilme.getUpdatedAt());
    }

    @Test
    void testUpdateFilmeNotFound() {
        UUID id = UUID.randomUUID();
        when(filmeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> filmeService.update(id, new Filme()));
    }

    @Test
    void testDelete() {
        UUID id = UUID.fromString(filme.getId());
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        filmeService.delete(id);
        assertNotNull(filme.getDeletedAt());
        verify(filmeRepository, times(1)).save(filme);
    }

    @Test
    void testDeleteAlreadyDeleted() {
        filme.setDeletedAt(LocalDateTime.now());
        UUID id = UUID.fromString(filme.getId());
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        filmeService.delete(id);
        verify(filmeRepository, never()).save(filme);
    }
}
