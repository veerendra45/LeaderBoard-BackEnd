package com.example.demo.Service;

import com.example.demo.Model.Student;
import com.example.demo.Repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepo studentRepo;

    StudentService(StudentRepo studentRepo){
        this.studentRepo = studentRepo;
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student getById(long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid Id Student not found"));
    }

    public Student saveStudent(Student student) {
        return studentRepo.save(student);
    }
}
