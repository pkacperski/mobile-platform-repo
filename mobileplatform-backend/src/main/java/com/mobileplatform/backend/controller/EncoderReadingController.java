package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.EncoderReading;
import com.mobileplatform.backend.service.EncoderReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/encoder-reading")
@RequiredArgsConstructor
public class EncoderReadingController {
    private final EncoderReadingService encoderReadingService;

    @GetMapping
    public List<EncoderReading> findAll() {
        return encoderReadingService.findAll();
    }

    @GetMapping("/newest")
    public Optional<EncoderReading> findNewest() {
        return encoderReadingService.findNewest();
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody EncoderReading encoderReading) {
        return encoderReadingService.save(encoderReading);
    }
}
