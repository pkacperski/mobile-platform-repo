package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.Vehicle;
import com.mobileplatform.backend.model.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Long save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle).getId();
    }
}
