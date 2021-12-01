package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.EncoderReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EncoderReadingRepository extends JpaRepository<EncoderReading, Long> {
    Optional<EncoderReading> findTopByOrderByIdDesc();
}
