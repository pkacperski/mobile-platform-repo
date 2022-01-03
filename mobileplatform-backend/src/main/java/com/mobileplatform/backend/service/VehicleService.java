package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.Vehicle;
import com.mobileplatform.backend.model.repository.VehicleRepository;
import com.mobileplatform.backend.websocket.WebSocketSampleServer;
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

        // TODO - handle adding new vehicle - send data to a new address(?), display data in a new tab
        WebSocketSampleServer.getInstance().send(WebSocketSampleServer.getGson().toJson(vehicle));

        return vehicleRepository.save(vehicle).getId();
    }
}
