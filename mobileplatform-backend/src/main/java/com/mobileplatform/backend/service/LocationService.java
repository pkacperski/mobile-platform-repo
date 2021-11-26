package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.Location;
import com.mobileplatform.backend.model.domain.Test;
import com.mobileplatform.backend.model.repository.LocationRepository;
import com.mobileplatform.backend.model.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> findNewest() {
        return locationRepository.findTopByOrderByIdDesc();
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }
}
