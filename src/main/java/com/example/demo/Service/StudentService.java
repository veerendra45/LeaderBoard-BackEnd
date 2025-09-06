package com.example.demo.Service;

import com.example.demo.Dto.ProfileSubmissionRequest;
import com.example.demo.Model.Platform;
import com.example.demo.Model.PlatformStats;
import com.example.demo.Model.Student;
import com.example.demo.Repository.PlatformRepo;
import com.example.demo.Repository.StudentRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final PlatformRepo platformRepo;
    private final PlatformStatsService platformStatsService;

    StudentService(StudentRepo studentRepo, PlatformRepo platformRepo, PlatformStatsService platformStatsService){
        this.studentRepo = studentRepo;
        this.platformRepo = platformRepo;
        this.platformStatsService = platformStatsService;
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student getById(long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid Id Student not found"));
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public Student saveStudent(Student student) {
        student.setPassword(encoder.encode(student.getPassword()));
        return studentRepo.save(student);
    }

    public Student addPlatformToStudent(Platform platform, long studentId) {
        Student student = studentRepo.findById(studentId).orElseThrow(
                () -> new RuntimeException("Student Notfound with Id"));
        platform.setStudent(student);
        student.getPlatforms().add(platform);
        return studentRepo.save(student);
    }


    public Student addPlatformStatsToStudent(PlatformStats platformstats, long studentId) {
        Student student = studentRepo.findById(studentId).orElseThrow(
                () -> new RuntimeException("Student Notfound with Id"));
        platformstats.setStudent(student);
        platformstats.calculateScore();
        student.setPlatformStats(platformstats);
        return studentRepo.save(student);
    }

    public Student submitProfile(ProfileSubmissionRequest request) {
        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setRollNumber(request.getRollNumber());
        student.setEmail(request.getEmail());
        student.setDepartment(request.getDepartment());
        student.setYear(request.getYear());
        student.setProfilePic(request.getProfilePic());

        if(student.getPassword() == null){
            student.setPassword("default@123");
        }

        Student savedStudent = studentRepo.save(student);

        for (ProfileSubmissionRequest.PlatformRequest platformReq : request.getPlatforms()) {
            Platform platform = new Platform();
            platform.setName(platformReq.getName());
            platform.setProfileUrl(platformReq.getProfileUrl());
            platform.setStudent(savedStudent);
            platformRepo.save(platform);

            platformStatsService.fetchAndSaveStats(platform, savedStudent);
        }
        return studentRepo.findById(savedStudent.getId()).orElseThrow(()
         -> new RuntimeException("Student not found"));
    }
}
