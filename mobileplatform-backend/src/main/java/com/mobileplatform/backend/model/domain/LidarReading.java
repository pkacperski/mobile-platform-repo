package com.mobileplatform.backend.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "lidar_reading")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LidarReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicle_id")
    @NotNull(message = "You have to specify the id of a known vehicle which sent data")
    private Long vehicleId;

    @Column(name = "reading_date")
    private LocalDateTime readingDate;

    @Column(name = "lidar_distances_reading")
    private String lidarDistancesReading; // traktowac jako String rozdzielony np. srednikami (w BD varchar(10000)), zeby uzywac to rozbic na liste za pomoca string.split()
}
