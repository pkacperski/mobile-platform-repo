package com.mobileplatform.backend.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "encoder_reading")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EncoderReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicle_id")
    @NotNull(message = "You have to specify the id of a known vehicle which sent data")
    private Long vehicleId;

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
