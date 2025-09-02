package com.example.demo.Service;

import com.example.demo.Model.Platform;
import com.example.demo.Model.Student;
import com.example.demo.Repository.PlatformRepo;
import com.example.demo.Repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformService {

    private final PlatformRepo platformRepo;
    private final StudentRepo studentRepo;

    PlatformService(PlatformRepo platformRepo, StudentRepo studentRepo){
        this.platformRepo = platformRepo;
        this.studentRepo = studentRepo;
    }

    public List<Platform> findAllPlatforms() {
        return platformRepo.findAll();
    }

    public Platform getById(long id) {
        return platformRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No such Id platform found"));
    }

    public Platform savePlatform(Platform platform, long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Invalid studentId"));
        platform.setStudent(student);
        return platformRepo.save(platform);
    }

    public void deleteById(long id) {
        if(!platformRepo.existsById(id)){
            throw new RuntimeException("Id not found");
        }
        platformRepo.deleteById(id);
    }
}
