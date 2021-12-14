package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.Location;
import com.mobileplatform.backend.model.repository.LocationRepository;
import com.mobileplatform.backend.websocket.WebSocketSampleServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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

    public Optional<Location> findNewestByVehicleId(Long vehicleId) {
        return locationRepository.findTopByOrderByIdDesc(vehicleId);
    }

    public ResponseEntity<String> save(@Valid Location location) {

        WebSocketSampleServer.getInstance().send(WebSocketSampleServer.getGson().toJson(location));

        locationRepository.save(location);
        return ResponseEntity.ok("Successfully saved to DB");
    }
}
