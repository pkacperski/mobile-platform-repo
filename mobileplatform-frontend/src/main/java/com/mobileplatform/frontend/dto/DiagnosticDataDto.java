package com.mobileplatform.frontend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class DiagnosticDataDto implements Serializable {

    private Long id;
    private Long vehicleId;
    private LocalDateTime readingDate;
    private Float wheelsTurnMeasure;
    private Float cameraTurnAngle;
    private Float batteryChargeStatus;
}
