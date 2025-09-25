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
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentRequest request, 
                                         Authentication auth) {
        String profEmail = auth.getName();
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        if (request.getCpf() == null || request.getCpf().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        String cpf = request.getCpf().replaceAll("[^0-9]", "");
        if (cpf.length() != 11) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Student student = studentService.createStudent(request, profEmail);
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.CREATED);
        }
        
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
}
