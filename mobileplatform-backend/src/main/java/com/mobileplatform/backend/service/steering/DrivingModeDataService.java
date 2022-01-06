package com.mobileplatform.backend.service.steering;

import com.mobileplatform.backend.model.domain.steering.DrivingModeData;
import com.mobileplatform.backend.model.repository.steering.DrivingModeDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrivingModeDataService {
    private final DrivingModeDataRepository drivingModeDataRepository;

    public Optional<DrivingModeData> findNewestByVehicleId(Long vehicleId) {
        return drivingModeDataRepository.findTopByOrderByIdDesc(vehicleId);
    }

    // return EmergencyModeData object instead of ResponseEntity<String> to make frontend able to parse the returned object (every time frontend executes performPost, a data model object should be returned)
    public DrivingModeData save(@Valid DrivingModeData drivingModeData) {
        return drivingModeDataRepository.save(drivingModeData);
    }
}
