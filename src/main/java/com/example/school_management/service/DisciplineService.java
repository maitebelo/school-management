package com.example.school_management.service;

import com.example.school_management.dto.DisciplineRequest;
import com.example.school_management.model.Discipline;
import com.example.school_management.repository.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineService {

    @Autowired
    private DisciplineRepository disciplineRepository;

    public Discipline createDiscipline(DisciplineRequest request, String profId) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (request.getCode() == null || request.getCode().trim().isEmpty()) {
            throw new RuntimeException("Code is required");
        }

        if (disciplineRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Discipline code already exists");
        }

        Discipline discipline = new Discipline(
            request.getName(),
            request.getCode()
        );

        return disciplineRepository.save(discipline);
    }

    public List<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    public Discipline getDisciplineById(String id) {
        return disciplineRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Discipline not found"));
    }
}
