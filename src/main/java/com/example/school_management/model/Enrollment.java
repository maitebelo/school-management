package com.example.school_management.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.time.LocalDateTime;

@Document(collection = "enrollments")
@CompoundIndexes({
    @CompoundIndex(name = "student_discipline_unique", def = "{'studentId': 1, 'disciplineId': 1}", unique = true)
})
public class Enrollment {
    @Id
    private String id;
    
    private String studentId;
    
    private String disciplineId;
    
    private Double grade;
    
    private String assignedBy;
    
    private LocalDateTime enrolledAt;

    public Enrollment() {
        this.enrolledAt = LocalDateTime.now();
    }

    public Enrollment(String studentId, String disciplineId) {
        this();
        this.studentId = studentId;
        this.disciplineId = disciplineId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}
