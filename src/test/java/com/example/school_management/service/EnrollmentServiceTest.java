package com.example.school_management.service;

import com.example.school_management.model.Enrollment;
import com.example.school_management.repository.EnrollmentRepository;
import com.example.school_management.repository.StudentRepository;
import com.example.school_management.repository.DisciplineRepository;
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
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment("student1", "discipline1");
        enrollment.setId("enrollment1");
    }

    @Test
    void allocate_success() {
        when(studentRepository.existsById("student1")).thenReturn(true);
        when(disciplineRepository.existsById("discipline1")).thenReturn(true);
        when(enrollmentRepository.existsByStudentIdAndDisciplineId("student1", "discipline1")).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = enrollmentService.allocate("student1", "discipline1", "professor1");

        assertNotNull(result);
        assertEquals("student1", result.getStudentId());
        assertEquals("discipline1", result.getDisciplineId());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void allocate_fail_student_not_found() {
        when(studentRepository.existsById("student1")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> 
            enrollmentService.allocate("student1", "discipline1", "professor1"));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void allocate_fail_discipline_not_found() {
        when(studentRepository.existsById("student1")).thenReturn(true);
        when(disciplineRepository.existsById("discipline1")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> 
            enrollmentService.allocate("student1", "discipline1", "professor1"));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void allocate_fail_duplicate_enrollment() {
        when(studentRepository.existsById("student1")).thenReturn(true);
        when(disciplineRepository.existsById("discipline1")).thenReturn(true);
        when(enrollmentRepository.existsByStudentIdAndDisciplineId("student1", "discipline1")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> 
            enrollmentService.allocate("student1", "discipline1", "professor1"));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void assignGrade_success() {
        when(enrollmentRepository.findById("enrollment1")).thenReturn(java.util.Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = enrollmentService.assignGrade("enrollment1", 8.5, "professor1");

        assertNotNull(result);
        assertEquals(8.5, result.getGrade());
        assertEquals("professor1", result.getAssignedBy());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void assignGrade_fail_enrollment_not_found() {
        when(enrollmentRepository.findById("enrollment1")).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            enrollmentService.assignGrade("enrollment1", 8.5, "professor1"));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void assignGrade_fail_invalid_grade_low() {
        when(enrollmentRepository.findById("enrollment1")).thenReturn(java.util.Optional.of(enrollment));

        assertThrows(RuntimeException.class, () -> 
            enrollmentService.assignGrade("enrollment1", -1.0, "professor1"));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void assignGrade_fail_invalid_grade_high() {
        when(enrollmentRepository.findById("enrollment1")).thenReturn(java.util.Optional.of(enrollment));

        assertThrows(RuntimeException.class, () -> 
            enrollmentService.assignGrade("enrollment1", 11.0, "professor1"));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void getApprovedStudents_returns_only_grades_ge_7() {
        List<Enrollment> enrollments = Arrays.asList(enrollment);
        when(enrollmentRepository.findByDisciplineIdAndGradeGreaterThanEqual("discipline1", 7.0)).thenReturn(enrollments);

        List<Enrollment> result = enrollmentService.getApprovedStudents("discipline1");

        assertEquals(1, result.size());
        verify(enrollmentRepository).findByDisciplineIdAndGradeGreaterThanEqual("discipline1", 7.0);
    }

    @Test
    void getFailedStudents_returns_only_grades_lt_7() {
        List<Enrollment> enrollments = Arrays.asList(enrollment);
        when(enrollmentRepository.findByDisciplineIdAndGradeLessThan("discipline1", 7.0)).thenReturn(enrollments);

        List<Enrollment> result = enrollmentService.getFailedStudents("discipline1");

        assertEquals(1, result.size());
        verify(enrollmentRepository).findByDisciplineIdAndGradeLessThan("discipline1", 7.0);
    }
}
