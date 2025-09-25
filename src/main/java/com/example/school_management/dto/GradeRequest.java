package com.example.school_management.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class GradeRequest {
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private Double grade;

    public GradeRequest() {}

    public GradeRequest(Double grade) {
        this.grade = grade;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
