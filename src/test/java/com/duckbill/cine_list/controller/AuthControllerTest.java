package com.duckbill.cine_list.controller;

import com.duckbill.cine_list.dto.LoginRequestDTO;
import com.duckbill.cine_list.dto.RegisterRequestDTO;
import com.duckbill.cine_list.dto.ResetPasswordRequest;
import com.duckbill.cine_list.dto.ResponseDTO;
import com.duckbill.cine_list.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UsuarioService usuarioService;

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
        ResponseDTO mockResponse = new ResponseDTO("Test Usuario", "mockedToken");
        when(usuarioService.login(loginRequest.email(), loginRequest.senha())).thenReturn(mockResponse);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseDTO responseBody = (ResponseDTO) response.getBody();
        assertEquals("Test Usuario", responseBody.nome());
        assertEquals("mockedToken", responseBody.token());
        verify(usuarioService, times(1)).login(loginRequest.email(), loginRequest.senha());
    }

    // Teste para login com falha (usuário ou senha inválidos)
    @Test
    void testLoginFailure() {
        when(usuarioService.login(loginRequest.email(), loginRequest.senha()))
                .thenThrow(new IllegalArgumentException("E-mail ou senha inválidos."));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioService, times(1)).login(loginRequest.email(), loginRequest.senha());
    }

    // Teste para registro com sucesso
    @Test
    void testRegisterSuccess() {
        ResponseDTO mockResponse = new ResponseDTO("Nome do Usuário", "mockedToken");
        when(usuarioService.register(any())).thenReturn(mockResponse);

        ResponseEntity<?> response = authController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseDTO responseBody = (ResponseDTO) response.getBody();
        assert responseBody != null;
        assertEquals(registerRequest.nome(), responseBody.nome());
        assertEquals("mockedToken", responseBody.token());
        verify(usuarioService, times(1)).register(any());
    }

    // Teste para registro com falha (usuário já existente)
    @Test
    void testRegisterUserAlreadyExists() {
        when(usuarioService.register(any())).thenThrow(new IllegalArgumentException("E-mail já está em uso."));

        ResponseEntity<?> response = authController.register(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioService, times(1)).register(any());
    }

    // TESTE PARA ENVIO DE UM EMAIL DE RECUPERAÇÃO
    @Test
    void testForgotPasswordSuccess() {
        // Simula um email enviado com sucesso
        doNothing().when(usuarioService).generatePasswordResetToken("test@usuario.com");

        ResponseEntity<?> response = authController.forgotPassword("test@usuario.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Instruções enviadas para o email.", response.getBody());
        verify(usuarioService, times(1)).generatePasswordResetToken("test@usuario.com");
    }

    @Test
    void testForgotPasswordUserNotFound() {
        // Configura o mock para lançar uma exceção ao tentar gerar o token para um email inexistente
        doThrow(new IllegalArgumentException("Usuário não encontrado"))
                .when(usuarioService).generatePasswordResetToken("notfound@usuario.com");

        // Executa o endpoint do controlador
        ResponseEntity<?> response = authController.generatePasswordResetToken("notfound@usuario.com");

        // Verifica se o retorno é HTTP 400 (BAD REQUEST)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verifica se a mensagem de erro está correta
        assertEquals("Usuário não encontrado", response.getBody());

        // Garante que o metodo foi chamado uma vez
        verify(usuarioService, times(1)).generatePasswordResetToken("notfound@usuario.com");
    }

    @Test
    void testResetPasswordExpiredToken() {
        doThrow(new IllegalArgumentException("Token expirado"))
                .when(usuarioService).resetPassword("expired-token", "new-password");

        ResponseEntity<?> response = authController.resetPassword("expired-token", "new-password");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token expirado", response.getBody());
        verify(usuarioService, times(1)).resetPassword("expired-token", "new-password");
    }

    @Test
    void testResetPasswordInvalidToken() {
        doThrow(new IllegalArgumentException("Token inválido"))
                .when(usuarioService).resetPassword("invalid-token", "new-password");

        ResponseEntity<?> response = authController.resetPassword("invalid-token", "new-password");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token inválido", response.getBody());
        verify(usuarioService, times(1)).resetPassword("invalid-token", "new-password");
    }

    @Test
    void testResetPasswordSuccess() {
        doNothing().when(usuarioService).resetPassword("valid-token", "new-password");

        ResponseEntity<?> response = authController.resetPassword("valid-token", "new-password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Senha redefinida com sucesso.", response.getBody());
        verify(usuarioService, times(1)).resetPassword("valid-token", "new-password");
    }
}