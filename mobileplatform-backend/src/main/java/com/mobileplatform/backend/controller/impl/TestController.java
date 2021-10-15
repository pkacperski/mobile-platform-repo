package com.mobileplatform.backend.controller.impl;

import com.mobileplatform.backend.controller.TestControllerApi;
import com.mobileplatform.backend.model.domain.Test;
import com.mobileplatform.backend.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController implements TestControllerApi {
    private final TestService testService;

    @Override
    public ResponseEntity test() {
        return ResponseEntity.ok(testService.findAll());
    }

    @Override
    public ResponseEntity findById(Integer id) {
        return ResponseEntity.ok(testService.findById(id));
    }

    @Override
    public ResponseEntity findByIdQueryParam(Integer id) {
        return ResponseEntity.ok(testService.findById(id));
    }

    @Override
    public ResponseEntity executePost(Test test) {
        return ResponseEntity.ok(testService.save(test));
    }
}
