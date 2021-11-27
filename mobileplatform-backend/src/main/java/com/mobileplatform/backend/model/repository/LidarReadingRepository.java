package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.LidarReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LidarReadingRepository extends JpaRepository<LidarReading, Long> {
    Optional<LidarReading> findTopByOrderByIdDesc();
}
