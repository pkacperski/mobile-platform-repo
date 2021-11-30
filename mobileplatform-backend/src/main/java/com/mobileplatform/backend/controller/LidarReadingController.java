package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.LidarReading;
import com.mobileplatform.backend.service.LidarReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lidar-reading")
@RequiredArgsConstructor
public class LidarReadingController {
    private final LidarReadingService lidarReadingService;

    @GetMapping
    public List<LidarReading> findAll() {
        return lidarReadingService.findAll();
    }

    @GetMapping("/newest")
    public Optional<LidarReading> findNewest() {
        return lidarReadingService.findNewest();
    }

    @PostMapping
    public void save(@Valid @RequestBody LidarReading lidarReading) {
        lidarReadingService.save(lidarReading);
    }
}
