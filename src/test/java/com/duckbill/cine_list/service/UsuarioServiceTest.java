package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
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
    private UsuarioRepository usuarioRepository; // simular o acesso ao banco de dados.

    /* O metodo setUp é executado antes de cada teste, criando um Usuario de exemplo com dados válidos.
       Usa when(usuarioRepository.findById(any())) para simular o metodo findById do repositório,
       retornando sempre um Optional com o usuário de exemplo ao buscar qualquer ID.*/
    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario();
        usuario.setId("123e4567-e89b-12d3-a456-426614174000"); // UUID válido
        usuario.setCpf("12345678909"); // CPF de exemplo válido
        usuario.setNome("Test User");
        usuario.setEmail("test@example.com");

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuario));
    }

    /* Teste para verificar se o metodo create de usuarioService lança uma exceção
         IllegalArgumentException ao tentar criar um usuário com um CPF inválido.*/
    @Test
    void testCreateUsuarioWithInvalidCpf() {
        Usuario usuario = new Usuario();
        usuario.setCpf("11111111111"); // CPF inválido

        assertThrows(IllegalArgumentException.class, () -> usuarioService.create(usuario));
    }

    /* Este teste verifica se o metodo getById retorna um Optional presente ao buscar um usuário
     com um ID válido (UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
     utilizando o usuário mock configurado em setUp.*/
    @Test
    void testGetById() {
        Optional<Usuario> usuario = usuarioService.getById(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        assertTrue(usuario.isPresent());
    }
}