package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.EncoderReading;
import com.mobileplatform.backend.model.repository.EncoderReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EncoderReadingService {
    private final EncoderReadingRepository encoderReadingRepository;

    public List<EncoderReading> findAll() {
        return encoderReadingRepository.findAll();
    }

    public Optional<EncoderReading> findNewest() {
        return encoderReadingRepository.findTopByOrderByIdDesc();
    }

    public EncoderReading save(EncoderReading encoderReading) {
        return encoderReadingRepository.save(encoderReading);
    }
}
