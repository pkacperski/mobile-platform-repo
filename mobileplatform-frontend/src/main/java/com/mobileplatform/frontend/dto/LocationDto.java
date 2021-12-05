package com.mobileplatform.frontend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class LocationDto implements Serializable {

    private Long id;
    private Long vehicleId;
    private LocalDateTime readingDate;

    private Double slamXCoordinate;
    private Double slamYCoordinate;
    private Double slamRotation;

    private Double realXCoordinate;
    private Double realYCoordinate;
}
