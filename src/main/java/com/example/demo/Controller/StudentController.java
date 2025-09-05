package com.example.demo.Controller;

import com.example.demo.Dto.ProfileSubmissionRequest;
import com.example.demo.Model.Platform;
import com.example.demo.Model.PlatformStats;
import com.example.demo.Model.Student;
import com.example.demo.Service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable long id){
        return studentService.getById(id);
    }

    @PostMapping
    public Student saveStudent(@RequestBody Student student){
        return studentService.saveStudent(student);
    }

    @PostMapping("/{studentId}/platforms")
    public Student addPlatformToStudent(@RequestBody Platform platform, @PathVariable long studentId){
        return studentService.addPlatformToStudent(platform, studentId);
    }

    @PostMapping("/{studentId}/platformstats")
    public Student addPlatformStatsToStudent(@RequestBody PlatformStats platformstats, @PathVariable long studentId){
        return studentService.addPlatformStatsToStudent(platformstats, studentId);
    }

    @PostMapping("/submit")
    public Student submitProfile(@RequestBody ProfileSubmissionRequest request){
        return studentService.submitProfile(request);
    }
}
