package com.mobileplatform.backend.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicle_data")
public class VehicleData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="vehicle_id")
    private Vehicle vehicle;

//    private String lidarDistanceReadings; // osobna encja, osobny endpoint
//    private String cameraPointCloudReadings; // osobna encja, osobny endpoint
    private String slamVehicleLocation; // TODO - rozdzielic(X, Y, rotacja) + wydzielic slam i real do osobnej encji
    private String realVehicleLocation;

    private String imuReadings; // rozbic na 9 kolumn - i wydzielic do nowej tabeli (IMU)
    private String encoderReadings; // rozbic na 4 floaty - i nowa tabel (wheel encoder readings?)
    private Short wheelsTurnMeasure;
    private Short cameraTurnAngle;
    private Short batteryChargeStatus;
    // TODO - jak przesylac takie sygnaly sterujace?

    // TODO - timestamp kiedy przyszla dana
}
