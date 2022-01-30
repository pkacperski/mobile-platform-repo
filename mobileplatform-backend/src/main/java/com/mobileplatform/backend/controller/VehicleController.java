package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.Vehicle;
import com.mobileplatform.backend.model.domain.VehicleConnectionStatus;
import com.mobileplatform.backend.opencv.VideoCaptureHandler;
import com.mobileplatform.backend.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.mobileplatform.backend.MobileplatformBackendApplication.IS_TEST_LIDAR_AND_PC_STREAMING;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping
    public List<Vehicle> findAll() {
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Vehicle> findById(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @PostMapping
    public Vehicle save(@RequestBody Vehicle vehicle) {

        if(vehicle.getConnectionStatus().equals(VehicleConnectionStatus.CONNECTED)) {
            int whichVehicle = Integer.parseInt(vehicle.getName().substring(vehicle.getName().indexOf('$') + 1));
            VideoCaptureHandler.createVideoStreams(whichVehicle, vehicle.getIpAddress());
            if(IS_TEST_LIDAR_AND_PC_STREAMING)
                VideoCaptureHandler.createMockLidarAndPointCloudStreams();
        }
        else if(vehicle.getConnectionStatus().equals(VehicleConnectionStatus.DISCONNECTED)) {
            int whichVehicle = Integer.parseInt(vehicle.getName().substring(vehicle.getName().indexOf('$') + 1));
            VideoCaptureHandler.disableVideoStreams(whichVehicle);
            if(IS_TEST_LIDAR_AND_PC_STREAMING)
                VideoCaptureHandler.disableLidarAndPointCloudMockStreams();
        }

        return vehicleService.save(vehicle);
    }
}
