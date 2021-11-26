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

    @Column(name = "slam_x")
    private Double slamX;
    @Column(name = "slam_y")
    private Double slamY;
    @Column(name = "slam_rotation")
    private Double slamRotation;

    @Column(name = "real_x")
    private Double realX;
    @Column(name = "real_y")
    private Double realY;
}
