package com.example.school_management.repository;

import com.example.school_management.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
