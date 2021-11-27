package com.mobileplatform.backend.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reading_date")
    private LocalDateTime readingDate;

    @Column(name = "slam_x_coordinate")
    private Double slamXCoordinate;
    @Column(name = "slam_y_coordinate")
    private Double slamYCoordinate;
    @Column(name = "slam_rotation")
    private Double slamRotation;

    @Column(name = "real_x_coordinate")
    private Double realXCoordinate;
    @Column(name = "real_y_coordinate")
    private Double realYCoordinate;
}
