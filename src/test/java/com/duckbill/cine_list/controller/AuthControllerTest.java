package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.dto.LoginRequestDTO;
import com.duckbill.cine_list.dto.LoginResponseDTO;
import com.duckbill.cine_list.dto.RegisterRequestDTO;
import com.duckbill.cine_list.dto.ResponseDTO;
import com.duckbill.cine_list.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private HttpServletResponse httpServletResponse;

    private LoginRequestDTO loginRequest;
    private RegisterRequestDTO registerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loginRequest = new LoginRequestDTO("test@usuario.com", "password");
        registerRequest = new RegisterRequestDTO("Nome do Usuário", "email@example.com", "39470950828", "senha123");
    }

    // Teste para login com sucesso
    @Test
    void testLoginSuccess() {
        LoginResponseDTO mockResponse = new LoginResponseDTO("Test Usuario", "test@usuario.com", "mockedToken");
        when(usuarioService.login(loginRequest.email(), loginRequest.senha())).thenReturn(mockResponse);

        ResponseEntity<?> response = authController.login(loginRequest, httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponseDTO responseBody = (LoginResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Test Usuario", responseBody.getNomeUsuario());
        assertEquals("test@usuario.com", responseBody.getEmailUsuario());
        assertEquals("mockedToken", responseBody.getToken());
        verify(usuarioService, times(1)).login(loginRequest.email(), loginRequest.senha());
        verify(httpServletResponse, times(1)).addCookie(any(Cookie.class));
    }

    // Teste para login com falha
    @Test
    void testLoginFailure() {
        when(usuarioService.login(loginRequest.email(), loginRequest.senha()))
                .thenThrow(new IllegalArgumentException("E-mail ou senha inválidos."));

        ResponseEntity<?> response = authController.login(loginRequest, httpServletResponse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioService, times(1)).login(loginRequest.email(), loginRequest.senha());
    }

    // Teste para registro com sucesso
    @Test
    void testRegisterSuccess() {
        ResponseDTO mockResponse = new ResponseDTO("Registro realizado com sucesso", "mockedToken");
        when(usuarioService.register(any())).thenReturn(mockResponse);

        ResponseEntity<?> response = authController.register(registerRequest, httpServletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseDTO responseBody = (ResponseDTO) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Registro realizado com sucesso", responseBody.getMessage());
        assertEquals("mockedToken", responseBody.getToken());
        verify(usuarioService, times(1)).register(any());
        verify(httpServletResponse, times(1)).addCookie(any(Cookie.class));
    }

    // Teste para registro com falha
    @Test
    void testRegisterUserAlreadyExists() {
        when(usuarioService.register(any())).thenThrow(new IllegalArgumentException("E-mail já está em uso."));

        ResponseEntity<?> response = authController.register(registerRequest, httpServletResponse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioService, times(1)).register(any());
    }
}
