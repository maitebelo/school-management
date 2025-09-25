package com.example.school_management.service;

import com.example.school_management.dto.DisciplineRequest;
import com.example.school_management.model.Discipline;
import com.example.school_management.repository.DisciplineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DisciplineServiceTest {

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineService disciplineService;

    private DisciplineRequest validRequest;
    private Discipline discipline;

    @BeforeEach
    void setUp() {
        validRequest = new DisciplineRequest();
        validRequest.setName("Mathematics");
        validRequest.setCode("MATH101");

        discipline = new Discipline("Mathematics", "MATH101");
    }

    @Test
    void createDiscipline_success() {
        when(disciplineRepository.existsByCode(anyString())).thenReturn(false);
        when(disciplineRepository.save(any(Discipline.class))).thenReturn(discipline);

        Discipline result = disciplineService.createDiscipline(validRequest, "professor1");

        assertNotNull(result);
        assertEquals("Mathematics", result.getName());
        assertEquals("MATH101", result.getCode());
        verify(disciplineRepository).save(any(Discipline.class));
    }

    @Test
    void createDiscipline_conflict_when_code_exists() {
        when(disciplineRepository.existsByCode(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> 
            disciplineService.createDiscipline(validRequest, "professor1"));
        verify(disciplineRepository, never()).save(any(Discipline.class));
    }

    @Test
    void createDiscipline_empty_name() {
        validRequest.setName("");

        assertThrows(RuntimeException.class, () -> 
            disciplineService.createDiscipline(validRequest, "professor1"));
        verify(disciplineRepository, never()).save(any(Discipline.class));
    }

    @Test
    void createDiscipline_empty_code() {
        validRequest.setCode("");

        assertThrows(RuntimeException.class, () -> 
            disciplineService.createDiscipline(validRequest, "professor1"));
        verify(disciplineRepository, never()).save(any(Discipline.class));
    }

    @Test
    void getAllDisciplines_returns_all() {
        List<Discipline> disciplines = Arrays.asList(discipline);
        when(disciplineRepository.findAll()).thenReturn(disciplines);

        List<Discipline> result = disciplineService.getAllDisciplines();

        assertEquals(1, result.size());
        assertEquals("Mathematics", result.get(0).getName());
    }

    @Test
    void getDisciplineById_success() {
        when(disciplineRepository.findById("1")).thenReturn(java.util.Optional.of(discipline));

        Discipline result = disciplineService.getDisciplineById("1");

        assertNotNull(result);
        assertEquals("Mathematics", result.getName());
    }

    @Test
    void getDisciplineById_not_found() {
        when(disciplineRepository.findById("1")).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            disciplineService.getDisciplineById("1"));
    }
}
