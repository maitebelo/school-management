package com.example.school_management.repository;

import com.example.school_management.model.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {
    Optional<Enrollment> findByStudentIdAndDisciplineId(String studentId, String disciplineId);
    boolean existsByStudentIdAndDisciplineId(String studentId, String disciplineId);
    List<Enrollment> findByDisciplineId(String disciplineId);
    List<Enrollment> findByDisciplineIdAndGradeGreaterThanEqual(String disciplineId, Double grade);
    List<Enrollment> findByDisciplineIdAndGradeLessThan(String disciplineId, Double grade);
}
