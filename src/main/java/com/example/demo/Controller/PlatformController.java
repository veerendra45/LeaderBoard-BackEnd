package com.example.demo.Controller;

import com.example.demo.Model.Platform;
import com.example.demo.Service.PlatformService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platforms")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping
    public List<Platform> getAllPlatforms(){
        return platformService.findAllPlatforms();
    }

    @GetMapping("/{id}")
    public Platform getById(@PathVariable long id){
        return platformService.getById(id);
    }

    @PostMapping("/students/{studentId}")
    public Platform savePlatform(@RequestBody Platform platform, @PathVariable long studentId){
        return platformService.savePlatform(platform, studentId);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable long id){
        platformService.deleteById(id);
        return "Succesfully deleted";
    }

}