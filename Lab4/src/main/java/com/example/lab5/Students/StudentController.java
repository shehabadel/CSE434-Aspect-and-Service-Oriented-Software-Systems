package com.example.lab5.Students;

import com.example.lab5.Students.dto.CreateStudentDTO;
import com.example.lab5.Students.dto.UpdateStudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(student -> new ResponseEntity<>(student, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody CreateStudentDTO studentDTO) {
        Student newStudent = studentService.createStudent(studentDTO);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody UpdateStudentDTO updateStudentDTO) {
        try {
            Student updatedStudent = studentService.updateStudent(id, updateStudentDTO);
            return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Find students by major
    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> students = studentService.findStudentsByMajor(major);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    
    // Find students with GPA greater than specified value
    @GetMapping("/gpa/{minGpa}")
    public ResponseEntity<List<Student>> getStudentsWithGpaGreaterThan(@PathVariable Double minGpa) {
        List<Student> students = studentService.findStudentsWithGpaGreaterThan(minGpa);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    
    // Find student by student number
    @GetMapping("/number/{studentNumber}")
    public ResponseEntity<Student> getStudentByStudentNumber(@PathVariable String studentNumber) {
        Student student = studentService.findStudentByStudentNumber(studentNumber);
        if (student != null) {
            return new ResponseEntity<>(student, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
} 