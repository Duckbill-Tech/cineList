package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.UsuarioDTO;
import com.duckbill.cine_list.mapper.UsuarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository; // Simula o acesso ao banco de dados.

    private UsuarioDTO usuarioDTO;
    private UUID usuarioId;

    /* O metodo setUp é executado antes de cada teste, criando um Usuario de exemplo com dados válidos.
       Usa when(usuarioRepository.findById(any())) para simular o metodo findById do repositório,
       retornando sempre um Optional com o usuário de exemplo ao buscar qualquer ID.*/
    @BeforeEach
    void setUp() {
        usuarioId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"); // UUID válido

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId); // Usa UUID diretamente
        usuario.setCpf("12345678909"); // CPF de exemplo válido
        usuario.setNome("Test User");
        usuario.setEmail("test@example.com");

        usuarioDTO = UsuarioMapper.toDto(usuario); // Cria o DTO baseado no usuário de exemplo

        when(usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(usuario));
    }

    /* Teste para verificar se o metodo create de usuarioService lança uma exceção
         IllegalArgumentException ao tentar criar um usuário com um CPF inválido.*/
    @Test
    void testCreateUsuarioWithInvalidCpf() {
        UsuarioDTO invalidUsuarioDTO = new UsuarioDTO();
        invalidUsuarioDTO.setCpf("11111111111"); // CPF inválido
        invalidUsuarioDTO.setNome("Invalid User");
        invalidUsuarioDTO.setEmail("invalid@example.com");

        assertThrows(IllegalArgumentException.class, () -> usuarioService.create(invalidUsuarioDTO));
    }

    /* Este teste verifica se o metodo getById retorna um Optional presente ao buscar um usuário
     com um ID válido (UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
     utilizando o usuário mock configurado em setUp.*/
    @Test
    void testGetById() {
        Optional<UsuarioDTO> usuario = usuarioService.getById(usuarioId);
        assertTrue(usuario.isPresent());
        assertEquals(usuarioDTO.getNome(), usuario.get().getNome());
        assertEquals(usuarioDTO.getEmail(), usuario.get().getEmail());
        assertEquals(usuarioDTO.getCpf(), usuario.get().getCpf());
    }
}