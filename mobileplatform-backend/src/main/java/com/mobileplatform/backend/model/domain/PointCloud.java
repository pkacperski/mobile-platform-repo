package com.mobileplatform.backend.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "point_cloud")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PointCloud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicle_id")
    @NotNull(message = "You have to specify the id of a known vehicle which sent data")
    private Long vehicleId;

    @Column(name = "reading_date")
    private LocalDateTime readingDate;

    /*
    * Podobnie jak z LidarReading - traktowac to jako ogromny string rozdzielony przecinkami i srednikami,
    * (PostgreSQL - varchar(1048576) LUB text(https://dba.stackexchange.com/questions/189876/size-limit-of-character-varying-postgresql),
    * H2 - varchar(1048576) LUB ew. CHARACTER LARGE OBJECT - http://www.h2database.com/html/datatypes.html)
    * a zeby z tego korzystac po odebraniu, to zrobic split() ze Stringa na List<PointCloudSinglePoint>
    * */
    @Column(name = "point_cloud_reading")
    private String pointCloudReading;
}
