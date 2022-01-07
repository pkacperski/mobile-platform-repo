package com.mobileplatform.frontend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class VehicleDto implements Serializable {

    private Long id;
    private String name;
    private String ipAddress;
    private LocalDateTime connectionDate;
}
