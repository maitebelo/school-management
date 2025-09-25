package com.example.school_management.repository;

import com.example.school_management.model.Professor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends MongoRepository<Professor, String> {
    Optional<Professor> findByEmail(String email);
    boolean existsByEmail(String email);
}
