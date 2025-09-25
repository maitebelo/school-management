package com.example.school_management.service;

import com.example.school_management.model.Enrollment;
import com.example.school_management.model.Student;
import com.example.school_management.repository.DisciplineRepository;
import com.example.school_management.repository.EnrollmentRepository;
import com.example.school_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<StudentGradeDTO> getApproved(String disciplineId) {
        if (!disciplineRepository.existsById(disciplineId)) {
            throw new RuntimeException("Discipline not found");
        }

        List<Enrollment> enrollments = enrollmentRepository
            .findByDisciplineIdAndGradeGreaterThanEqual(disciplineId, 7.0);

        return enrollments.stream()
            .map(this::mapToStudentGradeDTO)
            .collect(Collectors.toList());
    }

    public List<StudentGradeDTO> getFailed(String disciplineId) {
        if (!disciplineRepository.existsById(disciplineId)) {
            throw new RuntimeException("Discipline not found");
        }

        List<Enrollment> enrollments = enrollmentRepository
            .findByDisciplineIdAndGradeLessThan(disciplineId, 7.0);

        return enrollments.stream()
            .map(this::mapToStudentGradeDTO)
            .collect(Collectors.toList());
    }

    private StudentGradeDTO mapToStudentGradeDTO(Enrollment enrollment) {
        Student student = studentRepository.findById(enrollment.getStudentId())
            .orElseThrow(() -> new RuntimeException("Student not found"));
        
        return new StudentGradeDTO(
            student.getName(),
            student.getCpf(),
            student.getEmail(),
            student.getPhone(),
            student.getAddress(),
            enrollment.getGrade()
        );
    }

    public static class StudentGradeDTO {
        private String name;
        private String cpf;
        private String email;
        private String phone;
        private Student.Address address;
        private Double grade;

        public StudentGradeDTO(String name, String cpf, String email, String phone, 
                             Student.Address address, Double grade) {
            this.name = name;
            this.cpf = cpf;
            this.email = email;
            this.phone = phone;
            this.address = address;
            this.grade = grade;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public Student.Address getAddress() { return address; }
        public void setAddress(Student.Address address) { this.address = address; }
        public Double getGrade() { return grade; }
        public void setGrade(Double grade) { this.grade = grade; }
    }
}
