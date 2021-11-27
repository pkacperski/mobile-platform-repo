package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.Vehicle;
import com.mobileplatform.backend.model.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public Long save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle).getId();
    }
}
