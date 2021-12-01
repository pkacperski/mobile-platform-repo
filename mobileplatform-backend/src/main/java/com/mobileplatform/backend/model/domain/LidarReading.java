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
@Table(name = "lidar_reading")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LidarReading {
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

    @Column(name = "lidar_distances_reading")
    private String lidarDistancesReading; // traktowac jako String rozdzielony np. srednikami (w BD varchar(10000)), zeby uzywac to rozbic na liste za pomoca string.split()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LidarReading that = (LidarReading) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
