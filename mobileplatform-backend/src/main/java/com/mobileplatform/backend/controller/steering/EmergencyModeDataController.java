package com.mobileplatform.backend.controller.steering;

import com.mobileplatform.backend.model.domain.steering.EmergencyModeData;
import com.mobileplatform.backend.service.steering.EmergencyModeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/emergency-mode-data")
@RequiredArgsConstructor
public class EmergencyModeDataController {
    private final EmergencyModeDataService emergencyModeDataService;

    @GetMapping("/newest/{vehicleId}")
    public Optional<EmergencyModeData> findNewestByVehicleId(@PathVariable Long vehicleId) {
        return emergencyModeDataService.findNewestByVehicleId(vehicleId);
    }

    @PostMapping
    public EmergencyModeData save(@RequestBody EmergencyModeData emergencyModeData) {
        // return EmergencyModeData object instead of ResponseEntity<String> to make frontend able to parse the returned object (every time frontend executes performPost, a data model object should be returned)
        return emergencyModeDataService.save(emergencyModeData);
    }
}
