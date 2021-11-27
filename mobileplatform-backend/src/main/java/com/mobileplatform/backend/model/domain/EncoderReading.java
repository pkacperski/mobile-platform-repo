package com.mobileplatform.backend.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "encoder_reading")
public class EncoderReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reading_date")
    private LocalDateTime readingDate;

    @Column(name = "left_front_wheel_speed")
    private Double leftFrontWheelSpeed;
    @Column(name = "right_front_wheel_speed")
    private Double rightFrontWheelSpeed;
    @Column(name = "left_rear_wheel_speed")
    private Double leftRearWheelSpeed;
    @Column(name = "right_rear_wheel_speed")
    private Double rightRearWheelSpeed;
}
