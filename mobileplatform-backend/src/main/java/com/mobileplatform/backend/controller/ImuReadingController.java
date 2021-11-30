package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.ImuReading;
import com.mobileplatform.backend.service.ImuReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imu-reading")
@RequiredArgsConstructor
public class ImuReadingController {
    private final ImuReadingService imuDataService;

    @GetMapping
    public List<ImuReading> findAll() {
        return imuDataService.findAll();
    }

    @GetMapping("/newest")
    public Optional<ImuReading> findNewest() {
        return imuDataService.findNewest();
    }

    @PostMapping
    public void save(@Valid @RequestBody ImuReading imuData) {
        imuDataService.save(imuData);
    }
}