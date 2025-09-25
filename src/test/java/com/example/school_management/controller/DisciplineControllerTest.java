package com.example.school_management.controller;

import com.example.school_management.dto.DisciplineRequest;
import com.example.school_management.model.Discipline;
import com.example.school_management.service.DisciplineService;
import com.example.school_management.service.ReportService;
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
class DisciplineControllerTest {

    @Mock
    private DisciplineService disciplineService;

    @Mock
    private ReportService reportService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private DisciplineController disciplineController;

    private DisciplineRequest disciplineRequest;
    private Discipline discipline;
    private List<Discipline> disciplines;

    @BeforeEach
    void setUp() {
        disciplineRequest = new DisciplineRequest();
        disciplineRequest.setName("Matemática");
        disciplineRequest.setCode("MAT001");

        discipline = new Discipline("Matemática", "MAT001");
        disciplines = Arrays.asList(discipline);
    }

    @Test
    void testCreateDiscipline_Success() {
        when(authentication.getName()).thenReturn("professor@test.com");
        when(disciplineService.createDiscipline(any(DisciplineRequest.class), anyString())).thenReturn(discipline);

        ResponseEntity<Discipline> response = disciplineController.createDiscipline(disciplineRequest, authentication);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(discipline, response.getBody());
        verify(disciplineService).createDiscipline(disciplineRequest, "professor@test.com");
    }

    @Test
    void testCreateDiscipline_CodeExists() {
        when(authentication.getName()).thenReturn("professor@test.com");
        when(disciplineService.createDiscipline(any(DisciplineRequest.class), anyString()))
            .thenThrow(new RuntimeException("Discipline code already exists"));

        ResponseEntity<Discipline> response = disciplineController.createDiscipline(disciplineRequest, authentication);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testGetAllDisciplines_Success() {
        when(disciplineService.getAllDisciplines()).thenReturn(disciplines);

        ResponseEntity<List<Discipline>> response = disciplineController.getAllDisciplines();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(disciplines, response.getBody());
        verify(disciplineService).getAllDisciplines();
    }

    @Test
    void testGetApprovedStudents_Success() {
        List<ReportService.StudentGradeDTO> approvedStudents = Arrays.asList();
        when(reportService.getApproved(anyString())).thenReturn(approvedStudents);

        ResponseEntity<List<ReportService.StudentGradeDTO>> response = 
            disciplineController.getApprovedStudents("disciplineId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(approvedStudents, response.getBody());
        verify(reportService).getApproved("disciplineId");
    }

    @Test
    void testGetApprovedStudents_NotFound() {
        when(reportService.getApproved(anyString()))
            .thenThrow(new RuntimeException("Discipline not found"));

        ResponseEntity<List<ReportService.StudentGradeDTO>> response = 
            disciplineController.getApprovedStudents("disciplineId");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetFailedStudents_Success() {
        List<ReportService.StudentGradeDTO> failedStudents = Arrays.asList();
        when(reportService.getFailed(anyString())).thenReturn(failedStudents);

        ResponseEntity<List<ReportService.StudentGradeDTO>> response = 
            disciplineController.getFailedStudents("disciplineId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(failedStudents, response.getBody());
        verify(reportService).getFailed("disciplineId");
    }

    @Test
    void testGetFailedStudents_NotFound() {
        when(reportService.getFailed(anyString()))
            .thenThrow(new RuntimeException("Discipline not found"));

        ResponseEntity<List<ReportService.StudentGradeDTO>> response = 
            disciplineController.getFailedStudents("disciplineId");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
