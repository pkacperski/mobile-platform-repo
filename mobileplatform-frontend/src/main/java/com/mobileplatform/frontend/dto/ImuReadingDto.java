package com.mobileplatform.frontend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class ImuReadingDto implements Serializable {

    private Long id;
    private Long vehicleId;
    private LocalDateTime readingDate;

    private Double accelerationX;
    private Double accelerationY;
    private Double accelerationZ;

    private Double angularVelocityX;
    private Double angularVelocityY;
    private Double angularVelocityZ;

    private Double magneticFieldX;
    private Double magneticFieldY;
    private Double magneticFieldZ;
}
