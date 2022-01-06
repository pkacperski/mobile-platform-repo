package com.mobileplatform.backend.model.repository.steering;

import com.mobileplatform.backend.model.domain.steering.DrivingModeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DrivingModeDataRepository extends JpaRepository<DrivingModeData, Long> {

    @Query("SELECT d FROM DrivingModeData d WHERE d.id=(SELECT MAX(e.id) FROM DrivingModeData e WHERE e.vehicleId=:vehicleId)")
    Optional<DrivingModeData> findTopByOrderByIdDesc(Long vehicleId);
}
