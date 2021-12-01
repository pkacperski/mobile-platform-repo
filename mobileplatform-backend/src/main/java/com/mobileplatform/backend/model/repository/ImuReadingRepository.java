package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.ImuReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImuReadingRepository extends JpaRepository<ImuReading, Long> {
    Optional<ImuReading> findTopByOrderByIdDesc();
}
