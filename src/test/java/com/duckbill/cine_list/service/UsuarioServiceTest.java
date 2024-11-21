package com.duckbill.cine_list.service;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.UsuarioDTO;

import com.duckbill.cine_list.infra.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
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
    private EmailService emailService;

    private UsuarioDTO usuarioDTO;
    private UUID usuarioId;

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
    }

    @Test
    void testGetById() {
        Optional<UsuarioDTO> usuario = usuarioService.getById(usuarioId);
        assertTrue(usuario.isPresent());
        assertEquals(usuarioDTO.getNome(), usuario.get().getNome());
        assertEquals(usuarioDTO.getEmail(), usuario.get().getEmail());
        assertEquals(usuarioDTO.getCpf(), usuario.get().getCpf());
        assertEquals(usuarioDTO.getSenha(), usuario.get().getSenha());
    }

    @Test
    void testGenerateAndSendPasswordResetToken_Success() {
        // Configura um usuário de exemplo
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");

        // Configura os mocks
        when(usuarioRepository.findByEmail("test@example.com")).thenReturn(Optional.of(usuario));
        when(tokenService.generateToken(usuario)).thenReturn("mocked-token");
        doNothing().when(emailService).sendPasswordResetEmail(anyString(), anyString());

        // Chama o método
        String result = usuarioService.generateAndSendPasswordResetToken("test@example.com");

        // Verifica os resultados
        assertNotNull(result);
        assertEquals("mocked-token", result);
        verify(emailService, times(1)).sendPasswordResetEmail(eq("test@example.com"), eq("mocked-token"));
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testGenerateAndSendPasswordResetToken_EmailNotFound() {
        when(usuarioRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        String result = usuarioService.generateAndSendPasswordResetToken("notfound@example.com");

        assertNull(result);
        verify(emailService, never()).sendPasswordResetEmail(anyString(), anyString());
    }

    @Test
    void testResetPasswordWithToken_ValidToken() {
        String validToken = "validToken";
        Usuario usuario = new Usuario();
        usuario.setPasswordResetToken(validToken);
        usuario.setTokenExpirationTime(LocalDateTime.now().plusHours(1));

        when(usuarioRepository.findByPasswordResetToken(validToken)).thenReturn(Optional.of(usuario));

        boolean result = usuarioService.resetPasswordWithToken(validToken, "newPassword");

        assertTrue(result);
        assertNull(usuario.getPasswordResetToken());
        assertNull(usuario.getTokenExpirationTime());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testResetPasswordWithToken_ExpiredToken() {
        String expiredToken = "expiredToken";
        Usuario usuario = new Usuario();
        usuario.setPasswordResetToken(expiredToken);
        usuario.setTokenExpirationTime(LocalDateTime.now().minusHours(1));

        when(usuarioRepository.findByPasswordResetToken(expiredToken)).thenReturn(Optional.of(usuario));

        boolean result = usuarioService.resetPasswordWithToken(expiredToken, "newPassword");

        assertFalse(result);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testResetPasswordWithToken_InvalidToken() {
        when(usuarioRepository.findByPasswordResetToken("invalidToken")).thenReturn(Optional.empty());

        boolean result = usuarioService.resetPasswordWithToken("invalidToken", "newPassword");

        assertFalse(result);
        verify(usuarioRepository, never()).save(any());
    }
}