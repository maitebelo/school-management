package com.example.school_management.service;

import com.example.school_management.model.Enrollment;
import com.example.school_management.model.Student;
import com.example.school_management.repository.EnrollmentRepository;
import com.example.school_management.repository.DisciplineRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private ReportService reportService;

    private Enrollment enrollment;
    private Student student;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment("student1", "discipline1");
        enrollment.setGrade(8.5);

        student = new Student("John Doe", "12345678901", "john@example.com", "1234567890", 
            new Student.Address("123 Main St", "City", "12345"));
    }

    @Test
    void getApproved_returns_only_grades_ge_7() {
        when(disciplineRepository.existsById("discipline1")).thenReturn(true);
        when(enrollmentRepository.findByDisciplineIdAndGradeGreaterThanEqual("discipline1", 7.0))
            .thenReturn(Arrays.asList(enrollment));
        when(studentRepository.findById("student1")).thenReturn(java.util.Optional.of(student));

        List<ReportService.StudentGradeDTO> result = reportService.getApproved("discipline1");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals(8.5, result.get(0).getGrade());
    }

    @Test
    void getFailed_returns_only_grades_lt_7() {
        enrollment.setGrade(5.0);
        when(disciplineRepository.existsById("discipline1")).thenReturn(true);
        when(enrollmentRepository.findByDisciplineIdAndGradeLessThan("discipline1", 7.0))
            .thenReturn(Arrays.asList(enrollment));
        when(studentRepository.findById("student1")).thenReturn(java.util.Optional.of(student));

        List<ReportService.StudentGradeDTO> result = reportService.getFailed("discipline1");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals(5.0, result.get(0).getGrade());
    }

    @Test
    void getApproved_discipline_not_found() {
        when(disciplineRepository.existsById("discipline1")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> 
            reportService.getApproved("discipline1"));
    }

    @Test
    void getFailed_discipline_not_found() {
        when(disciplineRepository.existsById("discipline1")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> 
            reportService.getFailed("discipline1"));
    }

    @Test
    void reports_ignore_null_grades() {
        when(disciplineRepository.existsById("discipline1")).thenReturn(true);
        when(enrollmentRepository.findByDisciplineIdAndGradeGreaterThanEqual("discipline1", 7.0))
            .thenReturn(Arrays.asList());
        when(enrollmentRepository.findByDisciplineIdAndGradeLessThan("discipline1", 7.0))
            .thenReturn(Arrays.asList());

        List<ReportService.StudentGradeDTO> approved = reportService.getApproved("discipline1");
        List<ReportService.StudentGradeDTO> failed = reportService.getFailed("discipline1");

        assertEquals(0, approved.size());
        assertEquals(0, failed.size());
    }
}
