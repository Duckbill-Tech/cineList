//package com.duckbill.cine_list.controller;
//
//import com.duckbill.cine_list.dto.LoginRequestDTO;
//import com.duckbill.cine_list.dto.RegisterRequestDTO;
//import com.duckbill.cine_list.dto.ResetPasswordRequest;
//import com.duckbill.cine_list.dto.ResponseDTO;
//import com.duckbill.cine_list.service.UsuarioService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class AuthControllerTest {
//
//    @InjectMocks
//    private AuthController authController;
//
//    @Mock
//    private UsuarioService usuarioService;
//
//    private LoginRequestDTO loginRequest;
//    private RegisterRequestDTO registerRequest;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        loginRequest = new LoginRequestDTO("test@usuario.com", "password");
//        registerRequest = new RegisterRequestDTO("Nome do Usuário", "email@example.com", "39470950828", "senha123");
//    }
//
//    // Teste para login com sucesso
//    @Test
//    void testLoginSuccess() {
//        ResponseDTO mockResponse = new ResponseDTO("Test Usuario", "mockedToken");
//        when(usuarioService.login(loginRequest.email(), loginRequest.senha())).thenReturn(mockResponse);
//
//        ResponseEntity<?> response = authController.login(loginRequest);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        ResponseDTO responseBody = (ResponseDTO) response.getBody();
//        assert responseBody != null;
//        assertEquals("Test Usuario", responseBody.nome());
//        assertEquals("mockedToken", responseBody.token());
//        verify(usuarioService, times(1)).login(loginRequest.email(), loginRequest.senha());
//    }
//
//    // Teste para login com falha (usuário ou senha inválidos)
//    @Test
//    void testLoginFailure() {
//        when(usuarioService.login(loginRequest.email(), loginRequest.senha()))
//                .thenThrow(new IllegalArgumentException("E-mail ou senha inválidos."));
//
//        ResponseEntity<?> response = authController.login(loginRequest);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        verify(usuarioService, times(1)).login(loginRequest.email(), loginRequest.senha());
//    }
//
//    // Teste para registro com sucesso
//    @Test
//    void testRegisterSuccess() {
//        ResponseDTO mockResponse = new ResponseDTO("Nome do Usuário", "mockedToken");
//        when(usuarioService.register(any())).thenReturn(mockResponse);
//
//        ResponseEntity<?> response = authController.register(registerRequest);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        ResponseDTO responseBody = (ResponseDTO) response.getBody();
//        assert responseBody != null;
//        assertEquals(registerRequest.nome(), responseBody.nome());
//        assertEquals("mockedToken", responseBody.token());
//        verify(usuarioService, times(1)).register(any());
//    }
//
//    // Teste para registro com falha (usuário já existente)
//    @Test
//    void testRegisterUserAlreadyExists() {
//        when(usuarioService.register(any())).thenThrow(new IllegalArgumentException("E-mail já está em uso."));
//
//        ResponseEntity<?> response = authController.register(registerRequest);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        verify(usuarioService, times(1)).register(any());
//    }
//}