package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.DiagnosticData;
import com.mobileplatform.backend.model.repository.DiagnosticDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiagnosticDataService {
    private final DiagnosticDataRepository diagnosticDataRepository;

    public List<DiagnosticData> findAll() {
        return diagnosticDataRepository.findAll();
    }

    public Optional<DiagnosticData> findNewest() {
        return diagnosticDataRepository.findTopByOrderByIdDesc();
    }

    public Optional<DiagnosticData> findNewest(Long vehicleId) {
        return diagnosticDataRepository.findTopByOrderByIdDesc(vehicleId);
    }

    public ResponseEntity<String> save(@Valid DiagnosticData diagnosticData) {
        diagnosticDataRepository.save(diagnosticData);
        return ResponseEntity.ok("Successfully saved to DB");
    }
}
