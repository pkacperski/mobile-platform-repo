package com.mobileplatform.backend.model.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "point_cloud")
public class PointCloud {
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

    /*
    * Podobnie jak z LidarReading - traktowac to jako ogromny string rozdzielony przecinkami i srednikami,
    * (PostgreSQL - text(https://dba.stackexchange.com/questions/189876/size-limit-of-character-varying-postgresql),
    * H2 - varchar(1048576) LUB ew. CHARACTER LARGE OBJECT - http://www.h2database.com/html/datatypes.html)
    * a zeby z tego korzystac po odebraniu, to zrobic split() ze Stringa na List<PointCloudSinglePoint>
    * */
    @Column(name = "point_cloud_reading")
    private String pointCloudReading;
}
