package com.example.demo.Service;

import com.example.demo.Dto.LoginRequest;
import com.example.demo.Dto.ProfileSubmissionRequest;
import com.example.demo.Model.Platform;
import com.example.demo.Model.PlatformStats;
import com.example.demo.Model.Student;
import com.example.demo.Repository.PlatformRepo;
import com.example.demo.Repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final PlatformRepo platformRepo;
    private final PlatformStatsService platformStatsService;

    @Autowired
    private AuthenticationManager authManger;

    @Autowired
    private JWTService jwtService;

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

        if(studentRepo.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        if(studentRepo.existsByRollNumber(request.getRollNumber())){
            throw new IllegalArgumentException("Roll number already exists");
        }

        Student student = new Student();
        student.setFullName(request.getFullName());
        student.setRollNumber(request.getRollNumber());
        student.setEmail(request.getEmail());
        student.setDepartment(request.getDepartment());
        student.setYear(request.getYear());
        student.setProfilePic(request.getProfilePic());

        if(student.getPassword() == null || student.getPassword().isEmpty()){
            student.setPassword(encoder.encode("default@123"));
        } else {
            student.setPassword(encoder.encode(student.getPassword()));
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

    public String verify(LoginRequest loginInfo) {
        Authentication authentication =
                authManger.authenticate(new UsernamePasswordAuthenticationToken(loginInfo.getEmail().trim(), loginInfo.getPassword().trim()));

        if(authentication.isAuthenticated()){

            return jwtService.generateToken(loginInfo.getEmail());
        }
        return null;
    }

    public void updateLeaderboard(List<Student> students) {
        for (Student student : students) {
            // For each platform linked to student, fetch and update stats
            student.getPlatforms().forEach(platform -> {
                platformStatsService.fetchAndSaveStats(platform, student);
            });
            // Save updated student with refreshed PlatformStats
            studentRepo.save(student);
        }
        System.out.println("Leaderboard updated for " + students.size() + " students");
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledLeaderboardUpdate() {
        try {
            System.out.println("Midnight scheduler triggered - updating leaderboard...");
            List<Student> students = getAllStudents();
            updateLeaderboard(students);
            System.out.println("Leaderboard refresh completed at 12 AM");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Scheduler failed: " + e.getMessage());
        }
    }

}
