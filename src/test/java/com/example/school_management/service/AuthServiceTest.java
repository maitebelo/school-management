package com.example.school_management.service;

import com.example.school_management.dto.LoginRequest;
import com.example.school_management.dto.ProfessorRegisterRequest;
import com.example.school_management.model.Professor;
import com.example.school_management.repository.ProfessorRepository;
import com.example.school_management.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private ProfessorRegisterRequest registerRequest;
    private Professor professor;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new ProfessorRegisterRequest("John Doe", "john@example.com", "password123");
        professor = new Professor("John Doe", "john@example.com", "hashedPassword");
        loginRequest = new LoginRequest("john@example.com", "password123");
    }

    @Test
    void testRegisterProfessor_Success() {
        when(professorRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        Professor result = authService.register(registerRequest);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(professorRepository).save(any(Professor.class));
    }

    @Test
    void testRegisterProfessor_EmailAlreadyExists() {
        when(professorRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        verify(professorRepository, never()).save(any(Professor.class));
    }

    @Test
    void testLogin_Success() {
        when(professorRepository.findByEmail(anyString())).thenReturn(Optional.of(professor));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");

        var result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
    }

    @Test
    void testLogin_InvalidCredentials() {
        when(professorRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }
}
