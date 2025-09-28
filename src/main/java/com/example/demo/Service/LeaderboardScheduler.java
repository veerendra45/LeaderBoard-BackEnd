package com.example.demo.Service;

import com.example.demo.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardScheduler {
    @Autowired
    private StudentService studentService; // your service to fetch/update

    // Runs every day at 12:00 AM
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateLeaderboard() {
        System.out.println("Scheduler running at 12:00 AM...");

        //Fetch data from DB
        List<Student> students = studentService.getAllStudents();

        //Update leaderboard logic
        studentService.updateLeaderboard(students);

        System.out.println("Leaderboard updated successfully!");
    }
}
