package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.ImuReading;
import com.mobileplatform.backend.model.repository.ImuReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImuReadingService {
    private final ImuReadingRepository imuDataRepository;

    public List<ImuReading> findAll() {
        return imuDataRepository.findAll();
    }

    public Optional<ImuReading> findNewest() {
        return imuDataRepository.findTopByOrderByIdDesc();
    }

    public ImuReading save(ImuReading imuData) {
        return imuDataRepository.save(imuData);
    }
}
