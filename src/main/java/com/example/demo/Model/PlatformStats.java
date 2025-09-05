package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "platform_stats")
public class PlatformStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int easy;   // Number of easy problems solved
    private int medium; // Number of medium problems solved
    private int hard;   // Number of hard problems solved

    private int totalScore; // optional: easy=1, medium=2, hard=3

    // One-to-one relationship with student
    @OneToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference
    private Student student;

    // Method to calculate score
    public void calculateScore() {
        this.totalScore = easy * 1 + medium * 2 + hard * 3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEasy() {
        return easy;
    }

    public void setEasy(int easy) {
        this.easy = easy;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "ProblemStats{" +
                "id=" + id +
                ", easy=" + easy +
                ", medium=" + medium +
                ", hard=" + hard +
                ", totalScore=" + totalScore +
                '}';
    }

    public PlatformStats() {
    }

    public PlatformStats(Long id, int easy, int medium, int hard, int totalScore, Student student) {
        this.id = id;
        this.easy = easy;
        this.medium = medium;
        this.hard = hard;
        this.totalScore = totalScore;
        this.student = student;
    }


}
