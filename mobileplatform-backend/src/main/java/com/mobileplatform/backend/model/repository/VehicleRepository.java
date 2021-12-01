package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
