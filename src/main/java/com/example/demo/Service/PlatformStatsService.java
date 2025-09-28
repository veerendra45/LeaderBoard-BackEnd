package com.example.demo.Service;

import com.example.demo.Model.Platform;
import com.example.demo.Model.PlatformStats;
import com.example.demo.Model.Student;
import com.example.demo.Repository.PlatformStatsRepo;
import com.example.demo.Repository.StudentRepo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class PlatformStatsService {

    private final PlatformStatsRepo platformStatsRepo;
    private final StudentRepo studentRepo;
    private final RestTemplate restTemplate = new RestTemplate();

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
            existingStats.setEasy(existingStats.getEasy() + platformStats.getEasy());
            existingStats.setMedium(existingStats.getMedium() + platformStats.getMedium());
            existingStats.setHard(existingStats.getHard() + platformStats.getHard());
            existingStats.calculateScore();
            return platformStatsRepo.save(existingStats);
        }else{
            platformStats.setStudent(student);
            return platformStatsRepo.save(platformStats);
        }
    }

    public void fetchAndSaveStats(Platform platform, Student student) {
        String profileUrl = platform.getProfileUrl();
        String username = extractUsernameFromUrl(profileUrl);

        if (platform.getName().equalsIgnoreCase("LeetCode")) {
            fetchFromLeetCode(username, student);
        } else if (platform.getName().equalsIgnoreCase("GeeksforGeeks")) {
            fetchFromGFG(username, student);
        } else {
            throw new IllegalArgumentException("Currently only LeetCode and GFG are supported!");
        }
    }

    private void fetchFromLeetCode(String username, Student student) {
        String query = "query getUserProfile($username: String!) { " +
                "matchedUser(username: $username) { " +
                "submitStatsGlobal { acSubmissionNum { difficulty count } } } }";

        Map<String, Object> requestBody = Map.of(
                "query", query,
                "variables", Map.of("username", username)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "https://leetcode.com/graphql",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        Map<String, Object> matchedUser = (Map<String, Object>) data.get("matchedUser");
        Map<String, Object> submitStatsGlobal = (Map<String, Object>) matchedUser.get("submitStatsGlobal");
        List<Map<String, Object>> acSubmissionNum = (List<Map<String, Object>>) submitStatsGlobal.get("acSubmissionNum");

        int easy = 0, medium = 0, hard = 0;
        for (Map<String, Object> stat : acSubmissionNum) {
            String difficulty = (String) stat.get("difficulty");
            int count = (Integer) stat.get("count");
            switch (difficulty) {
                case "Easy" -> easy = count;
                case "Medium" -> medium = count;
                case "Hard" -> hard = count;
            }
        }

        saveStats(student, easy, medium, hard);
    }

    private void fetchFromGFG(String username, Student student) {
        // Unofficial GFG API
        String url = "https://geeks-for-geeks-api.vercel.app/" + username;

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> data = response.getBody();
        if (data == null || !data.containsKey("stats")) {
            throw new RuntimeException("No data received from GFG API for username: " + username);
        }

        Map<String, Object> stats = (Map<String, Object>) data.get("stats");

        int easy = (int) stats.getOrDefault("easy", 0);
        int medium = (int) stats.getOrDefault("medium", 0);
        int hard = (int) stats.getOrDefault("hard", 0);

        saveStats(student, easy, medium, hard);
    }

    private void saveStats(Student student, int easy, int medium, int hard) {
        PlatformStats stats = new PlatformStats();
        stats.setStudent(student);
        stats.setEasy(easy);
        stats.setMedium(medium);
        stats.setHard(hard);
        stats.calculateScore();

        savePlatformstat(stats, student.getId());
    }


    private String extractUsernameFromUrl(String url) {
        if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return url.substring(url.lastIndexOf("/") + 1);
    }
}

