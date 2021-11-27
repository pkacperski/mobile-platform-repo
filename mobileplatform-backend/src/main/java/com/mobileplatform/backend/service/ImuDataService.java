package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.ImuData;
import com.mobileplatform.backend.model.repository.ImuDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImuDataService {
    private final ImuDataRepository imuDataRepository;

    public List<ImuData> findAll() {
        return imuDataRepository.findAll();
    }

    public Optional<ImuData> findNewest() {
        return imuDataRepository.findTopByOrderByIdDesc();
    }

    public ImuData save(ImuData imuData) {
        return imuDataRepository.save(imuData);
    }
}
