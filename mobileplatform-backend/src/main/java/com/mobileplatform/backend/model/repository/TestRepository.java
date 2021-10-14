package com.mobileplatform.backend.model.repository;

import com.mobileplatform.backend.model.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Integer> {
}
