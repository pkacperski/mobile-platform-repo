package com.mobileplatform.backend.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "lidar_reading")
public class LidarReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reading_date")
    private LocalDateTime readingDate;

    @Column(name = "lidar_distances_reading")
    @ElementCollection
    private List<Double> lidarDistancesReading;
}
