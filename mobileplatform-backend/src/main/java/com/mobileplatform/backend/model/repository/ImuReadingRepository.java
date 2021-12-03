package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.ImuReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImuReadingRepository extends JpaRepository<ImuReading, Long> {
    Optional<ImuReading> findTopByOrderByIdDesc();

    @Query("SELECT d FROM ImuReading d WHERE d.id=(SELECT MAX(e.id) FROM ImuReading e WHERE e.vehicleId=:vehicleId)")
    Optional<ImuReading> findTopByOrderByIdDesc(Long vehicleId);
}
