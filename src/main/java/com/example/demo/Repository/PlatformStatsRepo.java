package com.example.demo.Repository;

import com.example.demo.Model.PlatformStats;
import com.example.demo.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformStatsRepo extends JpaRepository<PlatformStats, Long> {
    Optional<PlatformStats> findByStudent(Student student);
}
