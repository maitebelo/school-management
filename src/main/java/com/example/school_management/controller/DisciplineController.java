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
    public ResponseEntity<Discipline> createDiscipline(@Valid @RequestBody DisciplineRequest request, 
                                           Authentication auth) {
        String profEmail = auth.getName();
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Discipline disc = disciplineService.createDiscipline(request, profEmail);
        if (disc != null) {
            return new ResponseEntity<>(disc, HttpStatus.CREATED);
        }
        
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<Discipline>> getAllDisciplines() {
        List<Discipline> disciplines = disciplineService.getAllDisciplines();
        return new ResponseEntity<>(disciplines, HttpStatus.OK);
    }

    @GetMapping("/{disciplineId}/approved")
    public ResponseEntity<List<StudentGradeDTO>> getApprovedStudents(@PathVariable String disciplineId) {
        List<StudentGradeDTO> students = reportService.getApproved(disciplineId);
        if (students != null) {
            return ResponseEntity.ok(students);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{disciplineId}/failed")
    public ResponseEntity<List<StudentGradeDTO>> getFailedStudents(@PathVariable String disciplineId) {
        List<StudentGradeDTO> students = reportService.getFailed(disciplineId);
        if (students != null) {
            return ResponseEntity.ok(students);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
