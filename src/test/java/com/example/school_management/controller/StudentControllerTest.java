package com.example.school_management.controller;

import com.example.school_management.dto.StudentRequest;
import com.example.school_management.model.Student;
import com.example.school_management.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private StudentController studentController;

    private StudentRequest studentRequest;
    private Student student;
    private List<Student> students;

    @BeforeEach
    void setUp() {
        studentRequest = new StudentRequest();
        studentRequest.setName("Maria");
        studentRequest.setCpf("12345678901");
        studentRequest.setEmail("maria@test.com");
        studentRequest.setPhone("11999999999");

        student = new Student("Maria", "12345678901", "maria@test.com", "11999999999", null);
        students = Arrays.asList(student);
    }

    @Test
    void testCreateStudent_Success() {
        when(authentication.getName()).thenReturn("professor@test.com");
        when(studentService.createStudent(any(StudentRequest.class), anyString())).thenReturn(student);

        ResponseEntity<Student> response = studentController.createStudent(studentRequest, authentication);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(student, response.getBody());
        verify(studentService).createStudent(studentRequest, "professor@test.com");
    }

    @Test
    void testCreateStudent_InvalidCpf() {
        when(authentication.getName()).thenReturn("professor@test.com");
        when(studentService.createStudent(any(StudentRequest.class), anyString()))
            .thenThrow(new RuntimeException("Invalid CPF format"));

        ResponseEntity<Student> response = studentController.createStudent(studentRequest, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateStudent_CpfExists() {
        when(authentication.getName()).thenReturn("professor@test.com");
        when(studentService.createStudent(any(StudentRequest.class), anyString()))
            .thenThrow(new RuntimeException("CPF already exists"));

        ResponseEntity<Student> response = studentController.createStudent(studentRequest, authentication);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testGetAllStudents_Success() {
        when(studentService.getAllStudents()).thenReturn(students);

        ResponseEntity<List<Student>> response = studentController.getAllStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, response.getBody());
        verify(studentService).getAllStudents();
    }
}
