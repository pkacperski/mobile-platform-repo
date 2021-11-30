package com.mobileplatform.backend.model.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "diagnostic_data")
public class DiagnosticData {
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

    @Column(name = "whels_turn_measure")
    private Integer wheelsTurnMeasure;
    @Column(name = "camera_turn_angle")
    private Integer cameraTurnAngle;
    @Column(name = "battery_charge_status")
    private Integer batteryChargeStatus;
}
