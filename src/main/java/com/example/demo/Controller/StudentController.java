package com.example.demo.Controller;

import com.example.demo.Dto.LoginRequest;
import com.example.demo.Dto.ProfileSubmissionRequest;
import com.example.demo.Dto.StudentResponse;
import com.example.demo.Model.Platform;
import com.example.demo.Model.PlatformStats;
import com.example.demo.Model.Student;
import com.example.demo.Service.EmailService;
import com.example.demo.Service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final EmailService emailService;

    StudentController(StudentService studentService, EmailService emailService){
        this.studentService = studentService;
        this.emailService = emailService;
    }

    @GetMapping
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable long id){
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
        Student savedStudent = studentService.submitProfile(request);

        if(savedStudent.getEmail() != null){
            String subject = "Welcome to DSA Leaderboard 🎉";
            String body = "Hi " + savedStudent.getFullName() + ",\n\n"
                    + "Your profile has been submitted successfully!\n"
                    + "Keep practicing and track your progress on our leaderboard.\n\n"
                    + "Best,\nThe DSA Leaderboard Team";

            emailService.sendEmail(savedStudent.getEmail(), subject, body);
        }
        return savedStudent;
    }

    @PostMapping("/login")
    public ResponseEntity<?> verify(@RequestBody LoginRequest loginInfo){
        String token =  studentService.verify(loginInfo);
        if(token != null){
            return ResponseEntity.ok(Map.of(
                    "message", "Login Successful",
                    "token", token
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid email or password"));
    }
}
