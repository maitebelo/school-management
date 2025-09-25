package com.example.school_management.integration;

import com.example.school_management.dto.*;
import com.example.school_management.model.Professor;
import com.example.school_management.model.Student;
import com.example.school_management.model.Discipline;
import com.example.school_management.model.Enrollment;
import com.example.school_management.repository.*;
import com.example.school_management.service.AuthService;
import com.example.school_management.service.StudentService;
import com.example.school_management.service.DisciplineService;
import com.example.school_management.service.EnrollmentService;
import com.example.school_management.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SchoolManagementIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
        disciplineRepository.deleteAll();
        professorRepository.deleteAll();
    }

    @Test
    void completeFlow_registerProfessor_login_createStudent_createDiscipline_allocate_assignGrade_getApproved() {
        ProfessorRegisterRequest professorRequest = new ProfessorRegisterRequest("John Professor", "john@prof.com", "password123");
        Professor professor = authService.register(professorRequest);
        assertNotNull(professor.getId());

        LoginRequest loginRequest = new LoginRequest("john@prof.com", "password123");
        var loginResponse = authService.login(loginRequest);
        assertNotNull(loginResponse.getToken());

        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setName("Alice Student");
        studentRequest.setCpf("12345678901");
        studentRequest.setEmail("alice@student.com");
        studentRequest.setPhone("1234567890");
        studentRequest.setAddress(new Student.Address("123 Main St", "City", "12345"));

        Student student = studentService.createStudent(studentRequest, professor.getId());
        assertNotNull(student.getId());

        DisciplineRequest disciplineRequest = new DisciplineRequest();
        disciplineRequest.setName("Mathematics");
        disciplineRequest.setCode("MATH101");

        Discipline discipline = disciplineService.createDiscipline(disciplineRequest, professor.getId());
        assertNotNull(discipline.getId());

        Enrollment enrollment = enrollmentService.allocate(student.getId(), discipline.getId(), professor.getId());
        assertNotNull(enrollment.getId());

        enrollmentService.assignGrade(enrollment.getId(), 8.5, professor.getId());

        List<ReportService.StudentGradeDTO> approved = reportService.getApproved(discipline.getId());
        assertEquals(1, approved.size());
        assertEquals("Alice Student", approved.get(0).getName());
        assertEquals(8.5, approved.get(0).getGrade());
    }

    @Test
    void completeFlow_assignGradeLessThan7_getFailed() {
        ProfessorRegisterRequest professorRequest = new ProfessorRegisterRequest("John Professor", "john@prof.com", "password123");
        Professor professor = authService.register(professorRequest);

        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setName("Bob Student");
        studentRequest.setCpf("98765432101");
        studentRequest.setEmail("bob@student.com");
        studentRequest.setPhone("9876543210");
        studentRequest.setAddress(new Student.Address("456 Oak St", "Town", "54321"));

        Student student = studentService.createStudent(studentRequest, professor.getId());

        DisciplineRequest disciplineRequest = new DisciplineRequest();
        disciplineRequest.setName("Physics");
        disciplineRequest.setCode("PHYS101");

        Discipline discipline = disciplineService.createDiscipline(disciplineRequest, professor.getId());

        Enrollment enrollment = enrollmentService.allocate(student.getId(), discipline.getId(), professor.getId());
        enrollmentService.assignGrade(enrollment.getId(), 5.0, professor.getId());

        List<ReportService.StudentGradeDTO> failed = reportService.getFailed(discipline.getId());
        assertEquals(1, failed.size());
        assertEquals("Bob Student", failed.get(0).getName());
        assertEquals(5.0, failed.get(0).getGrade());
    }
}
