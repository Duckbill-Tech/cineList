package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.dto.LoginRequestDTO;
import com.duckbill.cine_list.dto.RegisterRequestDTO;
import com.duckbill.cine_list.dto.ResponseDTO;
import com.duckbill.cine_list.infra.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private Usuario usuario;
    private LoginRequestDTO loginRequest;
    private RegisterRequestDTO registerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID usuarioId = UUID.randomUUID();
        usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setEmail("test@usuario.com");
        usuario.setSenha("encodedPassword");
        usuario.setNome("Test Usuario");

        loginRequest = new LoginRequestDTO("test@usuario.com", "password");
        registerRequest = new RegisterRequestDTO("test@usuario.com", "password", "Test Usuario");
    }

    // Teste para login com sucesso
    @Test
    void testLoginSuccess() {
        when(usuarioRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequest.senha(), usuario.getSenha())).thenReturn(true);
        when(tokenService.generateToken(usuario)).thenReturn("mockedToken");

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseDTO responseBody = (ResponseDTO) response.getBody();
        assertEquals("Test Usuario", responseBody.nome());
        assertEquals("mockedToken", responseBody.token());
        verify(usuarioRepository, times(1)).findByEmail(loginRequest.email());
        verify(tokenService, times(1)).generateToken(usuario);
    }

    // Teste para login com senha incorreta
    @Test
    void testLoginInvalidPassword() {
        when(usuarioRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequest.senha(), usuario.getSenha())).thenReturn(false);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioRepository, times(1)).findByEmail(loginRequest.email());
        verify(tokenService, never()).generateToken(usuario);
    }

    // Teste para login com usuário não encontrado
    @Test
    void testLoginUserNotFound() {
        when(usuarioRepository.findByEmail(loginRequest.email())).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioRepository, times(1)).findByEmail(loginRequest.email());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    // Teste para registro com sucesso
    @Test
    void testRegisterSuccess() {
        when(usuarioRepository.findByEmail(registerRequest.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.senha())).thenReturn("encodedPassword");
        when(tokenService.generateToken(any(Usuario.class))).thenReturn("mockedToken");

        ResponseEntity<?> response = authController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseDTO responseBody = (ResponseDTO) response.getBody();
        assert responseBody != null;
        assertEquals(registerRequest.nome(), responseBody.nome());
        assertEquals("mockedToken", responseBody.token());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // Teste para registro com usuário já existente
    @Test
    void testRegisterUserAlreadyExists() {
        when(usuarioRepository.findByEmail(registerRequest.email())).thenReturn(Optional.of(usuario));

        ResponseEntity<?> response = authController.register(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}