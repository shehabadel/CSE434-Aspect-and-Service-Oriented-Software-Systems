package com.example.lab5.Students.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class CreateStudentDTO {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Student number is required")
    @Pattern(regexp = "^[A-Z0-9]{8,10}$", message = "Student number must be 8-10 alphanumeric characters")
    private String studentNumber;
    
    @NotBlank(message = "Major is required")
    private String major;
    
    @NotNull(message = "GPA is required")
    @Positive(message = "GPA must be positive")
    private Double gpa;
    
    // Default constructor
    public CreateStudentDTO() {
    }
    
    // Constructor with fields
    public CreateStudentDTO(String name, String email, String studentNumber, String major, Double gpa) {
        this.name = name;
        this.email = email;
        this.studentNumber = studentNumber;
        this.major = major;
        this.gpa = gpa;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStudentNumber() {
        return studentNumber;
    }
    
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public Double getGpa() {
        return gpa;
    }
    
    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }
} 