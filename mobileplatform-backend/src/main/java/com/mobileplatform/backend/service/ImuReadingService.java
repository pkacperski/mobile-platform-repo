package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.ImuReading;
import com.mobileplatform.backend.model.repository.ImuReadingRepository;
import com.mobileplatform.backend.websocket.TelemetryServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImuReadingService {
    private final ImuReadingRepository imuDataRepository;

    public List<ImuReading> findAll() {
        return imuDataRepository.findAll();
    }

    public Optional<ImuReading> findNewest() {
        return imuDataRepository.findTopByOrderByIdDesc();
    }

    public Optional<ImuReading> findNewestByVehicleId(Long vehicleId) {
        return imuDataRepository.findTopByOrderByIdDesc(vehicleId);
    }

    public ResponseEntity<String> save(@Valid ImuReading imuReading) {

        TelemetryServer.getInstance().send(TelemetryServer.getGson().toJson(imuReading));

        imuDataRepository.save(imuReading);
        return ResponseEntity.ok("Successfully saved to DB");
    }
}
