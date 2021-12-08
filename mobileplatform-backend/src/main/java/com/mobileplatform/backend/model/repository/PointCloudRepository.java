package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.PointCloud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointCloudRepository extends JpaRepository<PointCloud, Long> {
    Optional<PointCloud> findTopByOrderByIdDesc();

    @Query("SELECT d FROM PointCloud d WHERE d.id=(SELECT MAX(e.id) FROM PointCloud e WHERE e.vehicleId=:vehicleId)")
    Optional<PointCloud> findTopByOrderByIdDesc(Long vehicleId);
}
