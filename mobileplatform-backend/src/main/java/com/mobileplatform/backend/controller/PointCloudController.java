package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.PointCloud;
import com.mobileplatform.backend.service.PointCloudService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/point-cloud")
@RequiredArgsConstructor
public class PointCloudController {
    private final PointCloudService pointCloudService;

    @GetMapping
    public List<PointCloud> findAll() {
        return pointCloudService.findAll();
    }

    @GetMapping("/newest")
    public Optional<PointCloud> findNewest() {
        return pointCloudService.findNewest();
    }

    @PostMapping
    public void save(@RequestBody PointCloud pointCloud) {
        pointCloudService.save(pointCloud);
    }
}
