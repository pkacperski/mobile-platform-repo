package com.mobileplatform.backend.service;

import com.mobileplatform.backend.model.domain.Test;
import com.mobileplatform.backend.model.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository; // final!

    public List<Test> findAll() {
        return testRepository.findAll();
    }

    public Optional<Test> findById(Integer id) {
        return testRepository.findById(id);
    }

    public Test save(Test test) {
        return testRepository.save(test);
    }
}
