package com.mobileplatform.backend.model.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_name")
    private String vehicleName;
}