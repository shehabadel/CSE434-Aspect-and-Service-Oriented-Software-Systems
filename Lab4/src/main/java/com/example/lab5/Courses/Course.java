package com.example.lab5.Courses;

import com.example.lab5.Students.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String code;
    
    private String name;
    
    private String description;
    
    private Integer credits;
    
    @JsonIgnore // prevents infinite recursion when serializing to JSON.
    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
    
    public Course() {
    }
    
    public Course(String code, String name, String description, Integer credits) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.credits = credits;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getCredits() {
        return credits;
    }
    
    public void setCredits(Integer credits) {
        this.credits = credits;
    }
    
    public Set<Student> getStudents() {
        return students;
    }
    
    public void setStudents(Set<Student> students) {
        this.students = students;
    }
} 