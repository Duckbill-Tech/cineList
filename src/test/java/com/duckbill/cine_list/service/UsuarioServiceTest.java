package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.UsuarioDTO;
import com.duckbill.cine_list.mapper.UsuarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioDTO usuarioDTO;
    private UUID usuarioId;

    /* O metodo setUp é executado antes de cada teste, criando um Usuario de exemplo com dados válidos.
       Usa when(usuarioRepository.findById(any())) para simular o metodo findById do repositório,
       retornando sempre um Optional com o usuário de exemplo ao buscar qualquer ID.*/
    @BeforeEach
    void setUp() {
        usuarioId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setCpf("12345678909");
        usuario.setNome("Test User");
        usuario.setEmail("test@example.com");
        usuario.setSenha("testPassword");

        usuarioDTO = new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getSenha(),
                null,
                null,
                null
        );

        lenient().when(usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(usuario));
   }

    /* Teste para verificar se o metodo create de usuarioService lança uma exceção
         IllegalArgumentException ao tentar criar um usuário com um CPF inválido.*/
    @Test
    void testCreateUsuarioWithInvalidCpf() {
        UsuarioDTO invalidUsuarioDTO = new UsuarioDTO(null, "Invalid User", "invalid@example.com", "11111111111", "somePassword", null, null, null);
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
        assertEquals(usuarioDTO.getSenha(), usuario.get().getSenha());
    }
}