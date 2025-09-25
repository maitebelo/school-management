package com.example.school_management.controller;

import com.example.school_management.dto.LoginRequest;
import com.example.school_management.dto.LoginResponse;
import com.example.school_management.dto.ProfessorRegisterRequest;
import com.example.school_management.model.Professor;
import com.example.school_management.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ProfessorRegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Professor professor;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new ProfessorRegisterRequest("João", "joao@test.com", "senha123");
        loginRequest = new LoginRequest("joao@test.com", "senha123");
        professor = new Professor("João", "joao@test.com", "hashedPassword");
        loginResponse = new LoginResponse("jwt-token");
    }

    @Test
    void testRegister_Success() {
        when(authService.register(any(ProfessorRegisterRequest.class))).thenReturn(professor);

        ResponseEntity<Professor> response = authController.register(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(professor, response.getBody());
        verify(authService).register(registerRequest);
    }

    @Test
    void testLogin_Success() {
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginResponse, response.getBody());
        verify(authService).login(loginRequest);
    }
}
