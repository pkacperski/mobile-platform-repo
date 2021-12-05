package com.mobileplatform.frontend.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class PointCloudDto implements Serializable  {

    private Long id;
    private Long vehicleId;
    private LocalDateTime readingDate;
    /*
     * Podobnie jak z LidarReading - traktowac to jako ogromny string rozdzielony przecinkami i srednikami,
     * (PostgreSQL - varchar(1048576) LUB text(https://dba.stackexchange.com/questions/189876/size-limit-of-character-varying-postgresql),
     * H2 - varchar(1048576) LUB ew. CHARACTER LARGE OBJECT - http://www.h2database.com/html/datatypes.html)
     * a zeby z tego korzystac po odebraniu, to zrobic split() ze Stringa na List<PointCloudSinglePoint>
     * */
    private String pointCloudReading;
}
