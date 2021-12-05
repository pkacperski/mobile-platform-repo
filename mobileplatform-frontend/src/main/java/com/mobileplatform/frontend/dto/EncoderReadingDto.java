package com.mobileplatform.frontend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class EncoderReadingDto implements Serializable {

    private Long id;
    private Long vehicleId;
    private LocalDateTime readingDate;
    private Double leftFrontWheelSpeed;
    private Double rightFrontWheelSpeed;
    private Double leftRearWheelSpeed;
    private Double rightRearWheelSpeed;
}
