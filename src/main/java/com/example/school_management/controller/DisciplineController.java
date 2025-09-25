package com.example.school_management.controller;

import com.example.school_management.dto.DisciplineRequest;
import com.example.school_management.model.Discipline;
import com.example.school_management.service.DisciplineService;
import com.example.school_management.service.ReportService;
import com.example.school_management.service.ReportService.StudentGradeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplines")
public class DisciplineController {

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<Discipline> createDiscipline(@Valid @RequestBody DisciplineRequest request, Authentication authentication) {
        try {
            String professorEmail = authentication.getName();
            Discipline discipline = disciplineService.createDiscipline(request, professorEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(discipline);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Discipline>> getAllDisciplines() {
        List<Discipline> disciplines = disciplineService.getAllDisciplines();
        return ResponseEntity.ok(disciplines);
    }

    @GetMapping("/{disciplineId}/approved")
    public ResponseEntity<List<StudentGradeDTO>> getApprovedStudents(@PathVariable String disciplineId) {
        try {
            List<StudentGradeDTO> students = reportService.getApproved(disciplineId);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{disciplineId}/failed")
    public ResponseEntity<List<StudentGradeDTO>> getFailedStudents(@PathVariable String disciplineId) {
        try {
            List<StudentGradeDTO> students = reportService.getFailed(disciplineId);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
