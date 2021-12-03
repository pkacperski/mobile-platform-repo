package com.mobileplatform.backend.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_name")
    private String vehicleName;

    @Column(name = "connection_date")
    private LocalDateTime connectionDate;
}
