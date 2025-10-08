package com.example.demo.Dto;

import java.util.Map;

public class StudentResponse {
    private Long id;
    private String fullName;
    private String rollNumber;
    private int year;
    private String department;
    private String profilePicture;
    private Map<String, String> platforms; // leetcode, gfg, codechef
    private Map<String, Integer> platformStats; // easy, medium, hard, totalScore

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Map<String, String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Map<String, String> platforms) {
        this.platforms = platforms;
    }

    public Map<String, Integer> getPlatformStats() {
        return platformStats;
    }

    public void setPlatformStats(Map<String, Integer> platformStats) {
        this.platformStats = platformStats;
    }

    public StudentResponse(Long id, String fullName, String rollNumber, int year, String department, String profilePicture, Map<String, String> platforms, Map<String, Integer> platformStats) {
        this.id = id;
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.year = year;
        this.department = department;
        this.profilePicture = profilePicture;
        this.platforms = platforms;
        this.platformStats = platformStats;
    }

    public StudentResponse(){}
}
