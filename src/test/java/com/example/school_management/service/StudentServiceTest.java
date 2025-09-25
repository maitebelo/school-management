package com.example.school_management.service;

import com.example.school_management.dto.StudentRequest;
import com.example.school_management.model.Student;
import com.example.school_management.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private StudentRequest validRequest;
    private Student student;

    @BeforeEach
    void setUp() {
        validRequest = new StudentRequest();
        validRequest.setName("John Doe");
        validRequest.setCpf("12345678901");
        validRequest.setEmail("john@example.com");
        validRequest.setPhone("1234567890");
        validRequest.setAddress(new Student.Address("123 Main St", "City", "12345"));

        student = new Student("John Doe", "12345678901", "john@example.com", "1234567890", validRequest.getAddress());
    }

    @Test
    void createStudent_success() {
        when(studentRepository.existsByCpf(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.createStudent(validRequest, "professor1");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("12345678901", result.getCpf());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void createStudent_conflict_when_cpf_exists() {
        when(studentRepository.existsByCpf(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> 
            studentService.createStudent(validRequest, "professor1"));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void createStudent_invalid_cpf_format() {
        validRequest.setCpf("123");

        assertThrows(RuntimeException.class, () -> 
            studentService.createStudent(validRequest, "professor1"));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void createStudent_empty_name() {
        validRequest.setName("");

        assertThrows(RuntimeException.class, () -> 
            studentService.createStudent(validRequest, "professor1"));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void getAllStudents_returns_all() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void getStudentById_success() {
        when(studentRepository.findById("1")).thenReturn(java.util.Optional.of(student));

        Student result = studentService.getStudentById("1");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getStudentById_not_found() {
        when(studentRepository.findById("1")).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            studentService.getStudentById("1"));
    }
}
