package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.LidarReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LidarReadingRepository extends JpaRepository<LidarReading, Long> {
    Optional<LidarReading> findTopByOrderByIdDesc();

    @Query("SELECT d FROM LidarReading d WHERE d.id=(SELECT MAX(e.id) FROM LidarReading e WHERE e.vehicleId=:vehicleId)")
    Optional<LidarReading> findTopByOrderByIdDesc(Long vehicleId);
}
