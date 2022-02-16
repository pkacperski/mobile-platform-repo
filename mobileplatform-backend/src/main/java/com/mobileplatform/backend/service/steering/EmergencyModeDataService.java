package com.mobileplatform.backend.service.steering;

import com.mobileplatform.backend.model.domain.steering.EmergencyModeData;
import com.mobileplatform.backend.model.repository.steering.EmergencyModeDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmergencyModeDataService {
    private final EmergencyModeDataRepository emergencyModeDataRepository;

    public Optional<EmergencyModeData> findNewestByVehicleId(Long vehicleId) {
        return emergencyModeDataRepository.findTopByOrderByIdDesc(vehicleId);
    }

    public EmergencyModeData save(@Valid EmergencyModeData emergencyModeData) {
        return emergencyModeDataRepository.save(emergencyModeData);
    }
}
