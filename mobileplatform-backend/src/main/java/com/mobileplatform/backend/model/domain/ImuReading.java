package com.mobileplatform.backend.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "imu_reading")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ImuReading {
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

    @Column(name = "acceleration_x")
    private Double accelerationX;
    @Column(name = "acceleration_y")
    private Double accelerationY;
    @Column(name = "acceleration_z")
    private Double accelerationZ;

    @Column(name = "angular_velocity_x")
    private Double angularVelocityX;
    @Column(name = "angular_velocity_y")
    private Double angularVelocityY;
    @Column(name = "angular_velocity_z")
    private Double angularVelocityZ;

    @Column(name = "magnetic_field_x")
    private Double magneticFieldX;
    @Column(name = "magnetic_field_y")
    private Double magneticFieldY;
    @Column(name = "magnetic_field_z")
    private Double magneticFieldZ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ImuReading that = (ImuReading) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
