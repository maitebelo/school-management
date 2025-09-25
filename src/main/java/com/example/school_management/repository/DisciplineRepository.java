package com.example.school_management.repository;

import com.example.school_management.model.Discipline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisciplineRepository extends MongoRepository<Discipline, String> {
    Optional<Discipline> findByCode(String code);
    boolean existsByCode(String code);
}
