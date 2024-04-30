package com.example.t1security.controller;

import com.example.t1security.model.dto.LoginRequest;
import com.example.t1security.model.dto.RegisterRequest;
import com.example.t1security.service.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityControllerTest {

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private SecurityController securityController;

    private final String SUCCESS_TOKEN = "successToken";

    @Test
    public void testRegister() {
        RegisterRequest request = new RegisterRequest();
        when(securityService.registerUser(request)).thenReturn(SUCCESS_TOKEN);

        ResponseEntity<String> responseEntity = securityController.register(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(SUCCESS_TOKEN, responseEntity.getBody());
    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest();
        when(securityService.loginUser(request)).thenReturn(SUCCESS_TOKEN);

        ResponseEntity<String> responseEntity = securityController.login(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(SUCCESS_TOKEN, responseEntity.getBody());
    }
}
