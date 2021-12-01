package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.DiagnosticData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosticDataRepository extends JpaRepository<DiagnosticData, Long> {
    Optional<DiagnosticData> findTopByOrderByIdDesc();
}
