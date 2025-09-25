package com.example.school_management.dto;

import com.example.school_management.model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void testProfessorRegisterRequest() {
        ProfessorRegisterRequest request = new ProfessorRegisterRequest("João", "joao@test.com", "senha123");
        
        assertEquals("João", request.getName());
        assertEquals("joao@test.com", request.getEmail());
        assertEquals("senha123", request.getPassword());
        
        request.setName("Maria");
        request.setEmail("maria@test.com");
        request.setPassword("novaSenha");
        
        assertEquals("Maria", request.getName());
        assertEquals("maria@test.com", request.getEmail());
        assertEquals("novaSenha", request.getPassword());
    }

    @Test
    void testLoginRequest() {
        LoginRequest request = new LoginRequest("joao@test.com", "senha123");
        
        assertEquals("joao@test.com", request.getEmail());
        assertEquals("senha123", request.getPassword());
        
        request.setEmail("maria@test.com");
        request.setPassword("novaSenha");
        
        assertEquals("maria@test.com", request.getEmail());
        assertEquals("novaSenha", request.getPassword());
    }

    @Test
    void testLoginResponse() {
        LoginResponse response = new LoginResponse("jwt-token");
        
        assertEquals("jwt-token", response.getToken());
        
        response.setToken("new-token");
        assertEquals("new-token", response.getToken());
    }

    @Test
    void testStudentRequest() {
        Student.Address address = new Student.Address("Rua A", "Cidade", "12345");
        StudentRequest request = new StudentRequest("Maria", "12345678901", "maria@test.com", "11999999999", address);
        
        assertEquals("Maria", request.getName());
        assertEquals("12345678901", request.getCpf());
        assertEquals("maria@test.com", request.getEmail());
        assertEquals("11999999999", request.getPhone());
        assertEquals(address, request.getAddress());
        
        request.setName("João");
        request.setCpf("98765432100");
        request.setEmail("joao@test.com");
        request.setPhone("11888888888");
        
        assertEquals("João", request.getName());
        assertEquals("98765432100", request.getCpf());
        assertEquals("joao@test.com", request.getEmail());
        assertEquals("11888888888", request.getPhone());
    }

    @Test
    void testDisciplineRequest() {
        DisciplineRequest request = new DisciplineRequest("Matemática", "MAT001");
        
        assertEquals("Matemática", request.getName());
        assertEquals("MAT001", request.getCode());
        
        request.setName("Física");
        request.setCode("FIS001");
        
        assertEquals("Física", request.getName());
        assertEquals("FIS001", request.getCode());
    }

    @Test
    void testEnrollmentRequest() {
        EnrollmentRequest request = new EnrollmentRequest("studentId", "disciplineId");
        
        assertEquals("studentId", request.getStudentId());
        assertEquals("disciplineId", request.getDisciplineId());
        
        request.setStudentId("newStudentId");
        request.setDisciplineId("newDisciplineId");
        
        assertEquals("newStudentId", request.getStudentId());
        assertEquals("newDisciplineId", request.getDisciplineId());
    }

    @Test
    void testGradeRequest() {
        GradeRequest request = new GradeRequest(8.5);
        
        assertEquals(8.5, request.getGrade());
        
        request.setGrade(9.0);
        assertEquals(9.0, request.getGrade());
    }
}
