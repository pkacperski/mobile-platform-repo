package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findTopByOrderByIdDesc();

    @Query("SELECT d FROM Location d WHERE d.id=(SELECT MAX(e.id) FROM Location e WHERE e.vehicleId=:vehicleId)")
    Optional<Location> findTopByOrderByIdDesc(Long vehicleId);
}
