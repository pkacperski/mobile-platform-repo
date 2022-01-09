package com.mobileplatform.backend.controller.steering;

import com.mobileplatform.backend.model.domain.steering.DrivingModeData;
import com.mobileplatform.backend.service.steering.DrivingModeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/driving-mode-data")
@RequiredArgsConstructor
public class DrivingModeDataController {
    private final DrivingModeDataService drivingModeDataService;

    @GetMapping("/newest/{vehicleId}")
    public Optional<DrivingModeData> findNewestByVehicleId(@PathVariable Long vehicleId) {
        return drivingModeDataService.findNewestByVehicleId(vehicleId);
    }

    @PostMapping
    public DrivingModeData save(@RequestBody DrivingModeData drivingModeData) {
        // return EmergencyModeData object instead of ResponseEntity<String> to make frontend able to parse the returned object (every time frontend executes performPost, a data model object should be returned)
        return drivingModeDataService.save(drivingModeData);
    }
}
