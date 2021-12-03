package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.DiagnosticData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DiagnosticDataRepository extends JpaRepository<DiagnosticData, Long> {
    Optional<DiagnosticData> findTopByOrderByIdDesc();

    @Query("SELECT d FROM DiagnosticData d WHERE d.id=(SELECT MAX(e.id) FROM DiagnosticData e WHERE e.vehicleId=:vehicleId)")
    Optional<DiagnosticData> findTopByOrderByIdDesc(Long vehicleId);
}
