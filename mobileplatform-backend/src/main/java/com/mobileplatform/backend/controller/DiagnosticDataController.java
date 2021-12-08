package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.DiagnosticData;
import com.mobileplatform.backend.service.DiagnosticDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diagnostic-data")
@RequiredArgsConstructor
public class DiagnosticDataController {
    private final DiagnosticDataService diagnosticDataService;

    @GetMapping
    public List<DiagnosticData> findAll() {
        return diagnosticDataService.findAll();
    }

    @GetMapping("/newest")
    public Optional<DiagnosticData> findNewest() {
        return diagnosticDataService.findNewest();
    }

    @GetMapping("/newest/{vehicleId}")
    public Optional<DiagnosticData> findNewestByVehicleId(@PathVariable Long vehicleId) {
        return diagnosticDataService.findNewestByVehicleId(vehicleId);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody DiagnosticData diagnosticData) {
        return diagnosticDataService.save(diagnosticData);
    }
}
