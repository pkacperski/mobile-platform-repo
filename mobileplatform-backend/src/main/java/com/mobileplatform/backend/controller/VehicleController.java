package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.Vehicle;
import com.mobileplatform.backend.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping
    public List<Vehicle> findAll() {
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Vehicle> findById(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @PostMapping
    public Vehicle save(@RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle);
    }
}
