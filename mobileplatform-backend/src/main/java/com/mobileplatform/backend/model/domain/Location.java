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
@Table(name = "location")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Location {
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

    @Column(name = "slam_x_coordinate")
    private Double slamXCoordinate;
    @Column(name = "slam_y_coordinate")
    private Double slamYCoordinate;
    @Column(name = "slam_rotation")
    private Double slamRotation;

    @Column(name = "real_x_coordinate")
    private Double realXCoordinate;
    @Column(name = "real_y_coordinate")
    private Double realYCoordinate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Location location = (Location) o;
        return id != null && Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
