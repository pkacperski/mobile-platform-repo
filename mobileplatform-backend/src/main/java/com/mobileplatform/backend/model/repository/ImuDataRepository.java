package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.ImuData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImuDataRepository extends JpaRepository<ImuData, Long> {
    Optional<ImuData> findTopByOrderByIdDesc();
}
