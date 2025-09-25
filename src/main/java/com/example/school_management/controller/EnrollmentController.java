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
    public ResponseEntity<Enrollment> createEnrollment(@Valid @RequestBody EnrollmentRequest request, 
                                            Authentication auth) {
        String profEmail = auth.getName();
        
        if (request.getStudentId() == null || request.getDisciplineId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Enrollment enrollment = enrollmentService.allocate(request.getStudentId(), 
                                                         request.getDisciplineId(), 
                                                         profEmail);
        
        if (enrollment != null) {
            return new ResponseEntity<>(enrollment, HttpStatus.CREATED);
        }
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{enrollmentId}/grade")
    public ResponseEntity<Enrollment> assignGrade(@PathVariable String enrollmentId, 
                                       @Valid @RequestBody GradeRequest request,
                                       Authentication auth) {
        String profEmail = auth.getName();
        
        if (request.getGrade() < 0 || request.getGrade() > 10) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Enrollment enrollment = enrollmentService.assignGrade(enrollmentId, 
                                                            request.getGrade(), 
                                                            profEmail);
        
        if (enrollment != null) {
            return ResponseEntity.ok(enrollment);
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
