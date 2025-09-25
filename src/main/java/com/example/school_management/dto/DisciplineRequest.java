package com.example.school_management.dto;

import jakarta.validation.constraints.NotBlank;

public class DisciplineRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    private String code;

    public DisciplineRequest() {}

    public DisciplineRequest(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
