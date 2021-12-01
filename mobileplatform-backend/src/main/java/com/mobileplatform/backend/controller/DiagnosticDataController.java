package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.DiagnosticData;
import com.mobileplatform.backend.service.DiagnosticDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping
    public void save(@Valid @RequestBody DiagnosticData diagnosticData) {
        diagnosticDataService.save(diagnosticData);
    }
}
