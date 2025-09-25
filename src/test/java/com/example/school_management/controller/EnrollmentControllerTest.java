package com.example.school_management.controller;

import com.example.school_management.dto.EnrollmentRequest;
import com.example.school_management.dto.GradeRequest;
import com.example.school_management.model.Enrollment;
import com.example.school_management.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private EnrollmentController enrollmentController;

    private EnrollmentRequest enrollmentRequest;
    private GradeRequest gradeRequest;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setStudentId("studentId");
        enrollmentRequest.setDisciplineId("disciplineId");

        gradeRequest = new GradeRequest();
        gradeRequest.setGrade(8.5);

        enrollment = new Enrollment("studentId", "disciplineId");

        when(authentication.getName()).thenReturn("professor@test.com");
    }

    @Test
    void testCreateEnrollment_Success() {
        when(enrollmentService.allocate(anyString(), anyString(), anyString())).thenReturn(enrollment);

        ResponseEntity<Enrollment> response = enrollmentController.createEnrollment(enrollmentRequest, authentication);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(enrollment, response.getBody());
        verify(enrollmentService).allocate("studentId", "disciplineId", "professor@test.com");
    }

    @Test
    void testCreateEnrollment_StudentNotFound() {
        when(enrollmentService.allocate(anyString(), anyString(), anyString()))
            .thenThrow(new RuntimeException("Student not found"));

        ResponseEntity<Enrollment> response = enrollmentController.createEnrollment(enrollmentRequest, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateEnrollment_DisciplineNotFound() {
        when(enrollmentService.allocate(anyString(), anyString(), anyString()))
            .thenThrow(new RuntimeException("Discipline not found"));

        ResponseEntity<Enrollment> response = enrollmentController.createEnrollment(enrollmentRequest, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateEnrollment_AlreadyEnrolled() {
        when(enrollmentService.allocate(anyString(), anyString(), anyString()))
            .thenThrow(new RuntimeException("Student already enrolled in this discipline"));

        ResponseEntity<Enrollment> response = enrollmentController.createEnrollment(enrollmentRequest, authentication);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testAssignGrade_Success() {
        when(enrollmentService.assignGrade(anyString(), any(Double.class), anyString())).thenReturn(enrollment);

        ResponseEntity<Enrollment> response = enrollmentController.assignGrade("enrollmentId", gradeRequest, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enrollment, response.getBody());
        verify(enrollmentService).assignGrade("enrollmentId", 8.5, "professor@test.com");
    }

    @Test
    void testAssignGrade_EnrollmentNotFound() {
        when(enrollmentService.assignGrade(anyString(), any(Double.class), anyString()))
            .thenThrow(new RuntimeException("Enrollment not found"));

        ResponseEntity<Enrollment> response = enrollmentController.assignGrade("enrollmentId", gradeRequest, authentication);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAssignGrade_InvalidGrade() {
        when(enrollmentService.assignGrade(anyString(), any(Double.class), anyString()))
            .thenThrow(new RuntimeException("Grade must be between 0.0 and 10.0"));

        ResponseEntity<Enrollment> response = enrollmentController.assignGrade("enrollmentId", gradeRequest, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
