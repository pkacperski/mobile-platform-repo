package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.LidarReading;
import com.mobileplatform.backend.model.repository.LidarReadingRepository;
import com.mobileplatform.backend.websocket.TelemetryServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LidarReadingService {
    private final LidarReadingRepository lidarReadingRepository;

    public List<LidarReading> findAll() {
        return lidarReadingRepository.findAll();
    }

    public Optional<LidarReading> findNewest() {
        return lidarReadingRepository.findTopByOrderByIdDesc();
    }

    public Optional<LidarReading> findNewestByVehicleId(Long vehicleId) {
        return lidarReadingRepository.findTopByOrderByIdDesc(vehicleId);
    }

    public ResponseEntity<String> save(@Valid LidarReading lidarReading) {

        TelemetryServer.getInstance().send(TelemetryServer.getGson().toJson(lidarReading));

        lidarReadingRepository.save(lidarReading);
        return ResponseEntity.ok("Successfully saved to DB");
    }
}
