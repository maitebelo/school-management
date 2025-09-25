package com.example.school_management.dto;

import jakarta.validation.constraints.NotBlank;

public class EnrollmentRequest {
    @NotBlank
    private String studentId;
    
    @NotBlank
    private String disciplineId;

    public EnrollmentRequest() {}

    public EnrollmentRequest(String studentId, String disciplineId) {
        this.studentId = studentId;
        this.disciplineId = disciplineId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(String disciplineId) {
        this.disciplineId = disciplineId;
    }
}
