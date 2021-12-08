package com.mobileplatform.backend.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "location")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicle_id")
    @NotNull(message = "You have to specify the id of a known vehicle which sent data")
    private Long vehicleId;

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
