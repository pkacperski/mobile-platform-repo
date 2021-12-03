package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.PointCloud;
import com.mobileplatform.backend.model.repository.PointCloudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointCloudService {
    private final PointCloudRepository pointCloudRepository;

    public List<PointCloud> findAll() {
        return pointCloudRepository.findAll();
    }

    public Optional<PointCloud> findNewest() {
        return pointCloudRepository.findTopByOrderByIdDesc();
    }

    public ResponseEntity<String> save(@Valid PointCloud pointCloud) {
        pointCloudRepository.save(pointCloud);
        return ResponseEntity.ok("Successfully saved to DB");
    }
}
