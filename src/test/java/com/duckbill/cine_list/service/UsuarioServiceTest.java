package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.LoginResponseDTO;
import com.duckbill.cine_list.dto.ResponseDTO;
import com.duckbill.cine_list.dto.UsuarioDTO;
import com.duckbill.cine_list.infra.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        usuarioId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setCpf("12345678909");
        usuario.setNome("Test User");
        usuario.setEmail("test@example.com");
        usuario.setSenha("encodedPassword");

        usuarioDTO = new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                "testPassword",
                null,
                null,
                null
        );

        lenient().when(usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.of(usuario));
        lenient().when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        lenient().when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    }

    @Test
    void testRegisterWithValidData() {
        when(usuarioRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(tokenService.generateToken(any(Usuario.class))).thenReturn("mockedToken");

        ResponseDTO response = usuarioService.register(usuarioDTO);

        assertEquals("Test User", response.getMessage()); // Ajustado para usar getMessage()
        assertEquals("mockedToken", response.getToken());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(tokenService, times(1)).generateToken(any(Usuario.class));
    }

    @Test
    void testRegisterWithExistingEmail() {
        when(usuarioRepository.findByEmail(usuarioDTO.getEmail())).thenReturn(Optional.of(usuario));

        assertThrows(IllegalArgumentException.class, () -> usuarioService.register(usuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testRegisterWithInvalidCpf() {
        UsuarioDTO invalidUsuarioDTO = new UsuarioDTO(
                null,
                "Invalid User",
                "invalid@example.com",
                "11111111111",
                "somePassword",
                null,
                null,
                null
        );

        assertThrows(IllegalArgumentException.class, () -> usuarioService.register(invalidUsuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testLoginWithValidCredentials() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(tokenService.generateToken(any(Usuario.class))).thenReturn("mockedToken");

        LoginResponseDTO response = usuarioService.login(usuarioDTO.getEmail(), "testPassword");

        assertEquals("Test User", response.getNomeUsuario());
        assertEquals("test@example.com", response.getEmailUsuario());
        assertEquals("mockedToken", response.getToken());
        verify(passwordEncoder, times(1)).matches("testPassword", "encodedPassword");
    }

    @Test
    void testLoginWithInvalidEmail() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> usuarioService.login("invalid@example.com", "testPassword"));
    }

    @Test
    void testLoginWithInvalidPassword() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.login(usuarioDTO.getEmail(), "wrongPassword"));
    }

    @Test
    void testGetById() {
        Optional<UsuarioDTO> foundUsuario = usuarioService.getById(usuarioId);

        assertTrue(foundUsuario.isPresent());
        assertEquals("Test User", foundUsuario.get().getNome());
        assertEquals("test@example.com", foundUsuario.get().getEmail());
        assertEquals("12345678909", foundUsuario.get().getCpf());
    }

    @Test
    void testGetByIdNotFound() {
        when(usuarioRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Optional<UsuarioDTO> foundUsuario = usuarioService.getById(UUID.randomUUID());
        assertFalse(foundUsuario.isPresent());
    }

    @Test
    void testCreateUsuarioWithValidData() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO createdUsuario = usuarioService.create(usuarioDTO);

        assertEquals("Test User", createdUsuario.getNome());
        assertEquals("test@example.com", createdUsuario.getEmail());
        assertEquals("12345678909", createdUsuario.getCpf());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCreateUsuarioWithInvalidCpf() {
        UsuarioDTO invalidUsuarioDTO = new UsuarioDTO(
                null,
                "Invalid User",
                "invalid@example.com",
                "11111111111",
                "somePassword",
                null,
                null,
                null
        );

        assertThrows(IllegalArgumentException.class, () -> usuarioService.create(invalidUsuarioDTO));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
