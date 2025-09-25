package com.example.school_management.service;

import com.example.school_management.dto.LoginRequest;
import com.example.school_management.dto.LoginResponse;
import com.example.school_management.dto.ProfessorRegisterRequest;
import com.example.school_management.model.Professor;
import com.example.school_management.repository.ProfessorRepository;
import com.example.school_management.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Professor register(ProfessorRegisterRequest request) {
        if (professorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Professor professor = new Professor(
            request.getName(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );

        return professorRepository.save(professor);
    }

    public LoginResponse login(LoginRequest request) {
        Professor professor = professorRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), professor.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(professor.getEmail());
        return new LoginResponse(token);
    }
}
