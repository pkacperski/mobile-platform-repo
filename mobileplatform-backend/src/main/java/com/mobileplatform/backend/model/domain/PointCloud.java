package com.mobileplatform.backend.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "point_cloud")
public class PointCloud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reading_date")
    private LocalDateTime readingDate;

    @Column(name = "point_cloud_reading")
    @ElementCollection
    private List<Double> pointCloudReading; // TODO - consider transforming this into a List<PCReading> where PCReading is an object having 3 coordinates as doubles and 3 RGB values as ints
}
