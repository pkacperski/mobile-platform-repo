package com.mobileplatform.backend.model.domain.steering;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "driving_mode_data")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DrivingModeData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicle_id")
    @NotNull(message = "You have to specify the id of a known vehicle to which send steering data")
    private Long vehicleId;

    @Column(name = "signal_sending_date")
    private LocalDateTime signalSendingDate;

    @Column(name = "driving_mode")
    private DrivingMode drivingMode;
}

