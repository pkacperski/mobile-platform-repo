package com.mobileplatform.backend.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnostic_data")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DiagnosticData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicle_id")
    @NotNull(message = "You have to specify the id of a known vehicle which sent data")
    private Long vehicleId;

    @Column(name = "reading_date")
    private LocalDateTime readingDate;

    @Column(name = "wheels_turn_measure")
    private Float wheelsTurnMeasure;
    @Column(name = "camera_turn_angle")
    private Float cameraTurnAngle;
    @Column(name = "battery_charge_status")
    private Float batteryChargeStatus;
}
