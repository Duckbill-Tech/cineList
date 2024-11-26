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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmeServiceTest {

    @Mock
    private FilmeRepository filmeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private FilmeMapper filmeMapper;

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

        // Tornando o stubbing leniente para evitar a exceção em testes que não utilizam
        lenient().when(filmeMapper.toDto(filme)).thenReturn(filmeDTO);
    }

    /* O teste testCreate simula o comportamento do metodo save do filmeRepository,
       fazendo-o retornar o objeto filme sempre que é chamado com qualquer instância de Filme.
       Chama o metodo create do filmeService e verifica se:
        - id do createdFilme não é nulo.
        - título do createdFilme é igual ao título do filme mockado.
        - metodo save foi chamado exatamente uma vez.*/
    @Test
    void testCreate() {
        UUID usuarioId = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        // Simula a busca do usuário no repositório
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        // Simula o comportamento do save no repositório de filmes
        when(filmeRepository.save(any(Filme.class))).thenReturn(filme);

        FilmeDTO createdFilmeDTO = filmeService.create(filmeDTO, usuarioId);

        // Validações
        assertNotNull(createdFilmeDTO.getId());
        assertEquals(filmeDTO.getTitulo(), createdFilmeDTO.getTitulo());
        verify(filmeRepository, times(1)).save(any(Filme.class));
    }

    /* Testar getById com um filme válido e não deletado.
       Configura o mock para retornar filme quando findById for chamado com o id.
       Verifica se o filme é retornado e se o título do filme está correto.*/
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

        // Validações
        assertEquals(2, filmes.size());
        assertTrue(filmes.stream().allMatch(f -> f.getDeletedAt() == null));
    }

    /* Testa update, configurando o mock para que findById retorne o
       filme e save retorne o mesmo objeto após atualização.*/
    @Test
    void testUpdate() {
        UUID id = filme.getId();

        FilmeDTO updatedDetailsDTO = new FilmeDTO();
        updatedDetailsDTO.setId(id);
        updatedDetailsDTO.setTitulo("Updated Title");
        updatedDetailsDTO.setDescricao("Nova descrição");
        updatedDetailsDTO.setNota(9.0);

        // Mock do filme existente
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        // Simulação do comportamento do save para refletir as alterações
        when(filmeRepository.save(any(Filme.class))).thenAnswer(invocation -> {
            Filme filmeToUpdate = invocation.getArgument(0);
            filmeToUpdate.setTitulo(updatedDetailsDTO.getTitulo());
            filmeToUpdate.setDescricao(updatedDetailsDTO.getDescricao());
            filmeToUpdate.setNota(updatedDetailsDTO.getNota());
            filmeToUpdate.setUpdatedAt(LocalDateTime.now());
            return filmeToUpdate;
        });

        // Configuração do mapper para refletir o filme atualizado
        when(filmeMapper.toDto(any(Filme.class))).thenAnswer(invocation -> {
            Filme updatedFilme = invocation.getArgument(0);
            return new FilmeDTO(
                    updatedFilme.getId(),
                    updatedFilme.getTitulo(),
                    updatedFilme.getDescricao(),
                    updatedFilme.getNota(),
                    updatedFilme.getUpdatedAt(),
                    updatedFilme.getCompletedAt(),
                    updatedFilme.getDeletedAt(),
                    null
            );
        });

        // Execução do método update
        FilmeDTO updatedFilmeDTO = filmeService.update(id, updatedDetailsDTO);

        // Validações
        assertEquals("Updated Title", updatedFilmeDTO.getTitulo());
        assertEquals("Nova descrição", updatedFilmeDTO.getDescricao());
        assertEquals(9.0, updatedFilmeDTO.getNota());
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
        UUID id = filme.getId();
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        filmeService.delete(id);

        // Validações
        assertNotNull(filme.getDeletedAt());
        verify(filmeRepository, times(1)).save(filme);
    }

    /* Testa delete em um filme já deletado, garantindo que save não é chamado.*/
    @Test
    void testDeleteAlreadyDeleted() {
        filme.setDeletedAt(LocalDateTime.now());
        UUID id = filme.getId();
        when(filmeRepository.findById(id)).thenReturn(Optional.of(filme));

        filmeService.delete(id);

        // Verifica que o save não foi chamado
        verify(filmeRepository, never()).save(filme);
    }
}
