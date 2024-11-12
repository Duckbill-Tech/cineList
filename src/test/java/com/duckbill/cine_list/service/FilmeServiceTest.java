package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Filme;
import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.FilmeRepository;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.FilmeDTO;
import com.duckbill.cine_list.mapper.FilmeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FilmeServiceTest {


    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private FilmeMapper filmeMapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private FilmeService filmeService;

    private Filme filme;
    private FilmeDTO filmeDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID filmeId = UUID.randomUUID();

        filme = new Filme();
        filme.setId(filmeId);
        filme.setTitulo("Test Filme");
        filme.setDescricao("Descrição do Filme");
        filme.setNota(5.0);
        filme.setUpdatedAt(LocalDateTime.now());

        filmeDTO = new FilmeDTO(
                filme.getId(),
                filme.getTitulo(),
                filme.getDescricao(),
                filme.getNota(),
                filme.getUpdatedAt(),
                filme.getCompletedAt(),
                filme.getDeletedAt(),
                null
        );

        when(filmeMapper.toDto(filme)).thenReturn(filmeDTO);
        when(filmeMapper.toEntity(any(FilmeDTO.class), any())).thenReturn(filme);
    }

    /* O teste testCreate simula o comportamento do metodo save do filmeRepository,
     fazendo-o retornar o objeto filme sempre que é chamado com qualquer instância de Filme
     Chama o metodo create do filmeService e verifica se:
        - id do createdFilme não é nulo.
        - título do createdFilme é igual ao título do filme mockado.
        - metodo save foi chamado exatamente uma vez.*/
    @Test
    void testCreate() {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);

        FilmeDTO createdFilmeDTO = filmeService.create(filmeDTO, usuarioId);

        assertNotNull(createdFilmeDTO.getId());
        assertEquals(filmeDTO.getTitulo(), createdFilmeDTO.getTitulo());
        verify(filmeRepository, times(1)).save(any(Filme.class));
    }

    /* Testar getById com um filme válido e não deletado.
    Configura o mock para retornar filme quando findById for chamado com o id (se o filme é retornado e se o título
    do filme está correto).*/
    @Test
    void testGetByIdWithValidIdAndNotDeleted() {
        UUID id = filme.getId();
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        Optional<FilmeDTO> result = filmeService.getById(id);
        assertTrue(result.isPresent());
        assertEquals(filmeDTO.getTitulo(), result.get().getTitulo());
    }

    /* Verifica que um filme deletado não é retornado.*/
    @Test
    void testGetByIdWithDeletedFilme() {
        filme.setDeletedAt(LocalDateTime.now());
        UUID id = filme.getId();
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        Optional<FilmeDTO> result = filmeService.getById(id);
        assertFalse(result.isPresent());
    }


    /* Todos os filmes retornados não estão deletados.*/
    @Test
    void testGetAll() {
        Filme filme2 = new Filme();
        filme2.setId(UUID.randomUUID());
        filme2.setTitulo("Another Test Filme");
        filme2.setDescricao("Outra descrição");
        filme2.setNota(8.0);

        when(filmeRepository.findAll()).thenReturn(List.of(filme, filme2));
        when(filmeMapper.toDto(filme)).thenReturn(filmeDTO);
        when(filmeMapper.toDto(filme2)).thenReturn(new FilmeDTO(
                filme2.getId(),
                filme2.getTitulo(),
                filme2.getDescricao(),
                filme2.getNota(),
                filme2.getUpdatedAt(),
                filme2.getCompletedAt(),
                filme2.getDeletedAt(),
                null
        ));

        List<FilmeDTO> filmes = filmeService.getAll();
        assertEquals(2, filmes.size());
        assertTrue(filmes.stream().allMatch(f -> f.getDeletedAt() == null));
    }

    /*Testa update, configurando o mock para que findById retorne o
    filme e save retorne o mesmo objeto após atualização.*/
    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();

        Filme existingFilme = new Filme();
        existingFilme.setId(id);
        existingFilme.setTitulo("Test Filme");
        existingFilme.setDescricao("Descrição antiga");
        existingFilme.setNota(5.0);
        existingFilme.setUpdatedAt(LocalDateTime.now());

        FilmeDTO updatedDetailsDTO = new FilmeDTO();
        updatedDetailsDTO.setId(id);
        updatedDetailsDTO.setTitulo("Updated Title");
        updatedDetailsDTO.setDescricao("Nova descrição");
        updatedDetailsDTO.setNota(9.0);

        when(filmeRepository.findById(id)).thenReturn(Optional.of(existingFilme));

        when(filmeRepository.save(any(Filme.class))).thenAnswer(invocation -> {
            Filme filmeToSave = invocation.getArgument(0);
            filmeToSave.setTitulo(updatedDetailsDTO.getTitulo());
            filmeToSave.setDescricao(updatedDetailsDTO.getDescricao());
            filmeToSave.setNota(updatedDetailsDTO.getNota());
            filmeToSave.setUpdatedAt(LocalDateTime.now());
            return filmeToSave;
        });

        when(filmeMapper.toDto(any(Filme.class))).thenAnswer(invocation -> {
            Filme filme = invocation.getArgument(0);
            return new FilmeDTO(
                    filme.getId(),
                    filme.getTitulo(),
                    filme.getDescricao(),
                    filme.getNota(),
                    filme.getUpdatedAt(),
                    filme.getCompletedAt(),
                    filme.getDeletedAt(),
                    null
            );
        });

        FilmeDTO updatedFilmeDTO = filmeService.update(id, updatedDetailsDTO);

        assertEquals("Updated Title", updatedFilmeDTO.getTitulo());
        assertEquals("Nova descrição", updatedFilmeDTO.getDescricao());
        assertEquals(9.0, updatedFilmeDTO.getNota());
        assertNotNull(updatedFilmeDTO.getUpdatedAt(), "updatedAt deveria ter um valor");
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
        UUID id = filme.getId();
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        filmeService.delete(id);
        assertNotNull(filme.getDeletedAt());
        verify(filmeRepository, times(1)).save(filme);
    }

    /* Testa delete em um filme já deletado, garantindo que save não é chamado..*/
    @Test
    void testDeleteAlreadyDeleted() {
        filme.setDeletedAt(LocalDateTime.now());
        UUID id = filme.getId();
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        filmeService.delete(id);
        verify(filmeRepository, never()).save(filme);
    }
}
