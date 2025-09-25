package com.example.school_management.controller;

import com.example.school_management.dto.StudentRequest;
import com.example.school_management.model.Student;
import com.example.school_management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentRequest request, Authentication authentication) {
        try {
            String professorEmail = authentication.getName();
            Student student = studentService.createStudent(request, professorEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Invalid CPF format")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
}
