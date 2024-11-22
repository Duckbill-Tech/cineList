//package com.duckbill.cine_list.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.io.ByteArrayOutputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.mockito.Mockito.*;
//
//public class EmailServiceTest {
//
//    private EmailService emailService;
//
//    @Mock
//    private HttpURLConnection mockHttpURLConnection;
//
//    @Mock
//    private URL mockUrl;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        MockitoAnnotations.openMocks(this);
//
//        when(mockUrl.openConnection()).thenReturn(mockHttpURLConnection);
//        emailService = new EmailService("fake-api-key", "from@example.com", mockUrl);
//
//        doNothing().when(mockHttpURLConnection).setDoOutput(true);
//        doNothing().when(mockHttpURLConnection).setRequestMethod("POST");
//        doNothing().when(mockHttpURLConnection).setRequestProperty(anyString(), anyString());
//        when(mockHttpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_ACCEPTED);
//    }
//
//    @Test
//    void testSendPasswordResetEmailWithNullEmail() {
//        String token = "valid-token";
//
//        // Não deve lançar exceção
//        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail(null, token));
//    }
//
//    @Test
//    void testSendPasswordResetEmailWithEmptyToken() {
//        String email = "user@example.com";
//
//        // Não deve lançar exceção
//        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail(email, ""));
//    }
//
//    @Test
//    void testSendPasswordResetEmailValid() throws Exception {
//        String email = "user@example.com";
//        String token = "valid-token";
//
//        OutputStream outputStream = new ByteArrayOutputStream();
//        when(mockHttpURLConnection.getOutputStream()).thenReturn(outputStream);
//
//        // Chama o metodo de envio de email
//        emailService.sendPasswordResetEmail(email, token);
//
//        // Verifica se a conexão foi usada corretamente
//        verify(mockHttpURLConnection, times(1)).setDoOutput(true);
//        verify(mockHttpURLConnection, times(1)).getOutputStream();
//        verify(mockHttpURLConnection, times(1)).getResponseCode();
//    }
//}