package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.EncoderReading;
import com.mobileplatform.backend.model.repository.EncoderReadingRepository;
import com.mobileplatform.backend.websocket.WebSocketSampleServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EncoderReadingService {
    private final EncoderReadingRepository encoderReadingRepository;

    public List<EncoderReading> findAll() {
        return encoderReadingRepository.findAll();
    }

    public Optional<EncoderReading> findNewest() {
        return encoderReadingRepository.findTopByOrderByIdDesc();
    }

    public Optional<EncoderReading> findNewestByVehicleId(Long vehicleId) {
        return encoderReadingRepository.findTopByOrderByIdDesc(vehicleId);
    }

    public ResponseEntity<String> save(@Valid EncoderReading encoderReading) {

        WebSocketSampleServer.getInstance().send(WebSocketSampleServer.getGson().toJson(encoderReading));

        encoderReadingRepository.save(encoderReading);
        return ResponseEntity.ok("Successfully saved to DB");
    }
}
