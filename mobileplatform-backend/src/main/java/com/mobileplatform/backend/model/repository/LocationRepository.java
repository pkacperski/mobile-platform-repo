package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findTopByOrderByIdDesc();
}
