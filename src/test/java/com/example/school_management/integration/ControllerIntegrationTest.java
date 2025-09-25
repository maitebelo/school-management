package com.example.school_management.integration;

import com.example.school_management.dto.ProfessorRegisterRequest;
import com.example.school_management.dto.LoginRequest;
import com.example.school_management.dto.StudentRequest;
import com.example.school_management.dto.DisciplineRequest;
import com.example.school_management.dto.EnrollmentRequest;
import com.example.school_management.dto.GradeRequest;
import com.example.school_management.model.Professor;
import com.example.school_management.model.Student;
import com.example.school_management.model.Discipline;
import com.example.school_management.model.Enrollment;
import com.example.school_management.model.Student.Address;
import com.example.school_management.repository.ProfessorRepository;
import com.example.school_management.repository.StudentRepository;
import com.example.school_management.repository.DisciplineRepository;
import com.example.school_management.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.data.mongodb.uri=mongodb://localhost:27017/testdb"})
class ControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private String baseUrl;
    private String token;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        
        // Limpar dados de teste
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        disciplineRepository.deleteAll();
        professorRepository.deleteAll();
        
        // Registrar professor e fazer login
        ProfessorRegisterRequest registerRequest = new ProfessorRegisterRequest("João", "joao@test.com", "senha123");
        restTemplate.postForEntity(baseUrl + "/api/auth/register", registerRequest, Professor.class);
        
        LoginRequest loginRequest = new LoginRequest("joao@test.com", "senha123");
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(baseUrl + "/api/auth/login", loginRequest, String.class);
        
        // Extrair token da resposta
        String responseBody = loginResponse.getBody();
        if (responseBody != null && responseBody.contains("token")) {
            token = responseBody.substring(responseBody.indexOf(":") + 2, responseBody.indexOf("}") - 1);
        }
    }

    @Test
    void testCompleteFlow() {
        // 1. Criar aluno
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setName("Maria");
        studentRequest.setCpf("12345678901");
        studentRequest.setEmail("maria@test.com");
        studentRequest.setPhone("11999999999");
        studentRequest.setAddress(new Address("Rua A", "Cidade", "12345"));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<StudentRequest> studentEntity = new HttpEntity<>(studentRequest, headers);
        
        ResponseEntity<Student> studentResponse = restTemplate.postForEntity(
            baseUrl + "/api/students", studentEntity, Student.class);
        
        assertEquals(HttpStatus.CREATED, studentResponse.getStatusCode());
        assertNotNull(studentResponse.getBody());
        String studentId = studentResponse.getBody().getId();

        // 2. Criar disciplina
        DisciplineRequest disciplineRequest = new DisciplineRequest();
        disciplineRequest.setName("Matemática");
        disciplineRequest.setCode("MAT001");

        HttpEntity<DisciplineRequest> disciplineEntity = new HttpEntity<>(disciplineRequest, headers);
        
        ResponseEntity<Discipline> disciplineResponse = restTemplate.postForEntity(
            baseUrl + "/api/disciplines", disciplineEntity, Discipline.class);
        
        assertEquals(HttpStatus.CREATED, disciplineResponse.getStatusCode());
        assertNotNull(disciplineResponse.getBody());
        String disciplineId = disciplineResponse.getBody().getId();

        // 3. Alocar aluno na disciplina
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setStudentId(studentId);
        enrollmentRequest.setDisciplineId(disciplineId);

        HttpEntity<EnrollmentRequest> enrollmentEntity = new HttpEntity<>(enrollmentRequest, headers);
        
        ResponseEntity<Enrollment> enrollmentResponse = restTemplate.postForEntity(
            baseUrl + "/api/enrollments", enrollmentEntity, Enrollment.class);
        
        assertEquals(HttpStatus.CREATED, enrollmentResponse.getStatusCode());
        assertNotNull(enrollmentResponse.getBody());
        String enrollmentId = enrollmentResponse.getBody().getId();

        // 4. Atribuir nota
        GradeRequest gradeRequest = new GradeRequest();
        gradeRequest.setGrade(8.5);

        HttpEntity<GradeRequest> gradeEntity = new HttpEntity<>(gradeRequest, headers);
        
        ResponseEntity<Enrollment> gradeResponse = restTemplate.exchange(
            baseUrl + "/api/enrollments/" + enrollmentId + "/grade", 
            HttpMethod.PUT, gradeEntity, Enrollment.class);
        
        assertEquals(HttpStatus.OK, gradeResponse.getStatusCode());
        assertNotNull(gradeResponse.getBody());
        assertEquals(8.5, gradeResponse.getBody().getGrade());

        // 5. Listar alunos aprovados
        ResponseEntity<String> approvedResponse = restTemplate.exchange(
            baseUrl + "/api/disciplines/" + disciplineId + "/approved",
            HttpMethod.GET, new HttpEntity<>(headers), String.class);
        
        assertEquals(HttpStatus.OK, approvedResponse.getStatusCode());

        // 6. Listar alunos reprovados
        ResponseEntity<String> failedResponse = restTemplate.exchange(
            baseUrl + "/api/disciplines/" + disciplineId + "/failed",
            HttpMethod.GET, new HttpEntity<>(headers), String.class);
        
        assertEquals(HttpStatus.OK, failedResponse.getStatusCode());
    }
}
