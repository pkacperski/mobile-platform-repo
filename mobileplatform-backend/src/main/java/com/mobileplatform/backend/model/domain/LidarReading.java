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

    /*
     * Similar as with PointCloud - treat this as an enormous string split by newline characters and commas:
     * PostgreSQL - text - https://www.postgresqltutorial.com/postgresql-char-varchar-text/ https://dba.stackexchange.com/questions/189876/size-limit-of-character-varying-postgresql,
     * H2 - can handle maximally 1048576 characters in dev mode - http://www.h2database.com/html/datatypes.html
     * and after receiving, you can split() the received string to a List<Float> or List<Double>
     * */
    @Column(name = "lidar_distances_reading", columnDefinition = "text")
    private String lidarDistancesReading;
}
