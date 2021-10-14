package com.mobileplatform.backend.controller;

import com.mobileplatform.backend.model.domain.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "api/test", produces = MediaType.APPLICATION_JSON_VALUE)
public interface TestControllerApi {
    @GetMapping
    ResponseEntity test();

    @GetMapping("/{id}")
    ResponseEntity findById(@PathVariable Integer id);

    @GetMapping("/query")
    ResponseEntity findByIdQueryParam(@RequestParam Integer id);

    @PostMapping
    ResponseEntity executePost(@RequestBody Test test);
}
