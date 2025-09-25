package com.example.school_management.dto;

import com.example.school_management.model.Student.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StudentRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    private String cpf;
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String phone;
    
    private Address address;

    public StudentRequest() {}

    public StudentRequest(String name, String cpf, String email, String phone, Address address) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
