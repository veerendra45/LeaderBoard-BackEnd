package com.example.demo.Controller;

import com.example.demo.Model.PlatformStats;
import com.example.demo.Service.PlatformStatsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platformstats")
public class PlatformStatsController {

    private final PlatformStatsService platformStatsService;

    public PlatformStatsController(PlatformStatsService platformStatsService){
        this.platformStatsService = platformStatsService;
    }

    @GetMapping
    public List<PlatformStats> getAllPlatformStats(){
        return platformStatsService.getAllPlatformStats();
    }

    @GetMapping("/{id}")
    public PlatformStats getById(@PathVariable long id){
        return platformStatsService.getById(id);
    }

    @PostMapping("/student/{studentId}")
    public PlatformStats savePlatformstat(@RequestBody PlatformStats platformStats, @PathVariable long studentId){
        return platformStatsService.savePlatformstat(platformStats, studentId);
    }
}
