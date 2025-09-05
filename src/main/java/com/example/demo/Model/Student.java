package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String rollNumber;

    @Column(nullable = false)
    private int year; // e.g., 1, 2, 3, 4

    @Column(nullable = false)
    private String department;

    private String profilePic;

    // One student can have multiple platforms (LeetCode, GFG, CodeChef)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Platform> platforms;

    // One student has one ProblemStats object
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonManagedReference
    private PlatformStats platformStats;

    public Student() {
    }

    public Student(Long id, String email, String password, String fullName, String rollNumber, int year, String department, String profilePic, List<Platform> platforms, PlatformStats platformStats) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.year = year;
        this.department = department;
        this.profilePic = profilePic;
        this.platforms = platforms;
        this.platformStats = platformStats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public PlatformStats getPlatformStats() {
        return platformStats;
    }

    public void setPlatformStats(PlatformStats platformStats) {
        this.platformStats = platformStats;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", rollNumber='" + rollNumber + '\'' +
                ", year=" + year +
                ", department='" + department + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", platforms=" + platforms +
                ", platformStats=" + platformStats +
                '}';
    }
}
