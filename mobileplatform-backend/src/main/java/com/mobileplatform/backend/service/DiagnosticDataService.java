package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.DiagnosticData;
import com.mobileplatform.backend.model.repository.DiagnosticDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public DiagnosticData save(DiagnosticData diagnosticData) {
        return diagnosticDataRepository.save(diagnosticData);
    }
}
