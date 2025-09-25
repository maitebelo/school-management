package com.example.school_management.controller;

import com.example.school_management.dto.LoginRequest;
import com.example.school_management.dto.LoginResponse;
import com.example.school_management.dto.ProfessorRegisterRequest;
import com.example.school_management.model.Professor;
import com.example.school_management.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Professor> register(@Valid @RequestBody ProfessorRegisterRequest request) {
        Professor prof = authService.register(request);
        if (prof != null) {
            return new ResponseEntity<>(prof, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse resp = authService.login(request);
        if (resp != null) {
            return ResponseEntity.ok(resp);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
