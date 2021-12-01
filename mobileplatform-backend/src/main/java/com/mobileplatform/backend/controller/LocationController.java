package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.Location;
import com.mobileplatform.backend.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public List<Location> findAll() {
        return locationService.findAll();
    }

    @GetMapping("/newest")
    public Optional<Location> findNewest() {
        return locationService.findNewest();
    }

    @PostMapping
    public void save(@Valid @RequestBody Location location) {
        locationService.save(location);
    }
}
