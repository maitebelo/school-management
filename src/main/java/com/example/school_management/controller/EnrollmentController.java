package com.example.school_management.controller;

import com.example.school_management.dto.EnrollmentRequest;
import com.example.school_management.dto.GradeRequest;
import com.example.school_management.model.Enrollment;
import com.example.school_management.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<Enrollment> createEnrollment(@Valid @RequestBody EnrollmentRequest request, Authentication authentication) {
        try {
            String professorEmail = authentication.getName();
            Enrollment enrollment = enrollmentService.allocate(request.getStudentId(), request.getDisciplineId(), professorEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else if (e.getMessage().contains("already enrolled")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{enrollmentId}/grade")
    public ResponseEntity<Enrollment> assignGrade(@PathVariable String enrollmentId, 
                                                  @Valid @RequestBody GradeRequest request,
                                                  Authentication authentication) {
        try {
            String professorEmail = authentication.getName();
            Enrollment enrollment = enrollmentService.assignGrade(enrollmentId, request.getGrade(), professorEmail);
            return ResponseEntity.ok(enrollment);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
