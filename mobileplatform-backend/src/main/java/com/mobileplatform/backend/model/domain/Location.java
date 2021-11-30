package com.mobileplatform.backend.model.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="vehicle_id")
    @ManyToOne
    @NotNull(message = "You have to specify the id of a known vehicle which sent data")
    // TODO - fix! @NotNull nie dziala - mozna dodawac w swaggerze nowy obiekt bez podawania Vehicle i przechodzi - Vehicle jest wtedy nullem
    private Vehicle vehicle;

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
