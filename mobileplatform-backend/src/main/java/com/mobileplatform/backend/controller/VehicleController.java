package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.Vehicle;
import com.mobileplatform.backend.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    public Long save(@RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle);
    }
}
