package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.EncoderReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EncoderReadingRepository extends JpaRepository<EncoderReading, Long> {
    Optional<EncoderReading> findTopByOrderByIdDesc();

    @Query("SELECT d FROM EncoderReading d WHERE d.id=(SELECT MAX(e.id) FROM EncoderReading e WHERE e.vehicleId=:vehicleId)")
    Optional<EncoderReading> findTopByOrderByIdDesc(Long vehicleId);
}
