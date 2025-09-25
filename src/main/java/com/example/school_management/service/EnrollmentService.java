package com.example.school_management.service;

import com.example.school_management.dto.EnrollmentRequest;
import com.example.school_management.dto.GradeRequest;
import com.example.school_management.model.Enrollment;
import com.example.school_management.repository.EnrollmentRepository;
import com.example.school_management.repository.StudentRepository;
import com.example.school_management.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    public Enrollment allocate(String studentId, String disciplineId, String professorId) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }

        if (!disciplineRepository.existsById(disciplineId)) {
            throw new RuntimeException("Discipline not found");
        }

        if (enrollmentRepository.existsByStudentIdAndDisciplineId(studentId, disciplineId)) {
            throw new RuntimeException("Student already enrolled in this discipline");
        }

        Enrollment enrollment = new Enrollment(studentId, disciplineId);
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment assignGrade(String enrollmentId, Double grade, String professorId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (grade < 0.0 || grade > 10.0) {
            throw new RuntimeException("Grade must be between 0.0 and 10.0");
        }

        enrollment.setGrade(grade);
        enrollment.setAssignedBy(professorId);

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getApprovedStudents(String disciplineId) {
        return enrollmentRepository.findByDisciplineIdAndGradeGreaterThanEqual(disciplineId, 7.0);
    }

    public List<Enrollment> getFailedStudents(String disciplineId) {
        return enrollmentRepository.findByDisciplineIdAndGradeLessThan(disciplineId, 7.0);
    }
}
