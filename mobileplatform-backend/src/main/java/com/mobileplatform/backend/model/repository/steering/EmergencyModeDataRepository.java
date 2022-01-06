package com.mobileplatform.backend.model.repository.steering;

import com.mobileplatform.backend.model.domain.steering.EmergencyModeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmergencyModeDataRepository extends JpaRepository<EmergencyModeData, Long> {

    @Query("SELECT d FROM EmergencyModeData d WHERE d.id=(SELECT MAX(e.id) FROM EmergencyModeData e WHERE e.vehicleId=:vehicleId)")
    Optional<EmergencyModeData> findTopByOrderByIdDesc(Long vehicleId);
}
