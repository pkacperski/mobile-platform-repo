package com.mobileplatform.frontend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class LidarReadingDto implements Serializable {

    private Long id;
    private Long vehicleId;
    private LocalDateTime readingDate;
    private String lidarDistancesReading; // traktowac jako String rozdzielony np. srednikami (w BD varchar(10000)), zeby uzywac to rozbic na liste za pomoca string.split()
}
