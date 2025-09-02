package com.example.demo.Service;

import com.example.demo.Model.PlatformStats;
import com.example.demo.Model.Student;
import com.example.demo.Repository.PlatformStatsRepo;
import com.example.demo.Repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformStatsService {

    private final PlatformStatsRepo platformStatsRepo;
    private final StudentRepo studentRepo;

    public PlatformStatsService(PlatformStatsRepo platformStatsRepo, StudentRepo studentRepo){
        this.platformStatsRepo = platformStatsRepo;
        this.studentRepo = studentRepo;
    }

    public List<PlatformStats> getAllPlatformStats() {
        return platformStatsRepo.findAll();
    }

    public PlatformStats getById(long id) {
        return platformStatsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlatformStats with this Id is not found"));
    }

    public PlatformStats savePlatformstat(PlatformStats platformStats, long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        platformStats.calculateScore();
        PlatformStats existingStats = platformStatsRepo.findByStudent(student).orElse(null);
        if(existingStats != null){
            existingStats.setEasy(platformStats.getEasy() + platformStats.getEasy());
            existingStats.setMedium(platformStats.getMedium() + platformStats.getMedium());
            existingStats.setHard(platformStats.getHard() + platformStats.getHard());
            existingStats.setTotalScore(platformStats.getTotalScore() + platformStats.getTotalScore());
            return platformStatsRepo.save(existingStats);
        }else{
            platformStats.setStudent(student);
            return platformStatsRepo.save(platformStats);
        }
    }
}
