package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.PointCloud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointCloudRepository extends JpaRepository<PointCloud, Long> {
    Optional<PointCloud> findTopByOrderByIdDesc();
}
