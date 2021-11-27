package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.ImuData;
import com.mobileplatform.backend.service.ImuDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imu-data")
@RequiredArgsConstructor
public class ImuDataController {
    private final ImuDataService imuDataService;

    @GetMapping
    public List<ImuData> findAll() {
        return imuDataService.findAll();
    }

    @GetMapping("/newest")
    public Optional<ImuData> findNewest() {
        return imuDataService.findNewest();
    }

    @PostMapping
    public void save(@RequestBody ImuData imuData) {
        imuDataService.save(imuData);
    }
}
