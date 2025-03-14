package com.example.lab5.Students;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Find students by major
    List<Student> findByMajor(String major);
    
    // Find students with GPA greater than the specified value
    List<Student> findByGpaGreaterThan(Double gpa);
    
    // Find student by student number
    Student findByStudentNumber(String studentNumber);
} 