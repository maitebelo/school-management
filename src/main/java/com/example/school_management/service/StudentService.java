package com.example.school_management.service;

import com.example.school_management.dto.StudentRequest;
import com.example.school_management.model.Student;
import com.example.school_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(StudentRequest request, String profId) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }

        String cpf = request.getCpf().replaceAll("[^0-9]", "");
        if (cpf.length() != 11) {
            throw new RuntimeException("Invalid CPF format");
        }

        if (studentRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF already exists");
        }

        Student student = new Student(
            request.getName(),
            request.getCpf(),
            request.getEmail(),
            request.getPhone(),
            request.getAddress()
        );

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(String id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}
