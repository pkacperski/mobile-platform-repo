package com.mobileplatform.backend.model.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "point_cloud_reading")
    @ElementCollection
    private List<Float> pointCloudReading; // TODO - consider transforming this into a List<PCReading> where PCReading is an object having 3 coordinates as doubles and 3 RGB values as ints
    // TODO - laczenie @OneToMany i wlasny typ PointCloud; Double lub Float
}
