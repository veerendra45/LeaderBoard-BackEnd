package com.example.demo.Repository;

import com.example.demo.Model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformRepo extends JpaRepository<Platform, Long> {
}
