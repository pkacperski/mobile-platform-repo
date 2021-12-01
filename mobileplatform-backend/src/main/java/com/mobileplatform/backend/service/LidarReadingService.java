package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.LidarReading;
import com.mobileplatform.backend.model.repository.LidarReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LidarReadingService {
    private final LidarReadingRepository lidarReadingRepository;

    public List<LidarReading> findAll() {
        return lidarReadingRepository.findAll();
    }

    public Optional<LidarReading> findNewest() {
        return lidarReadingRepository.findTopByOrderByIdDesc();
    }

    public LidarReading save(LidarReading lidarReading) {
        return lidarReadingRepository.save(lidarReading);
    }
}
