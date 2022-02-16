package com.mobileplatform.frontend.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class PointCloudDto implements Serializable  {

    private Long id;
    private Long vehicleId;
    private LocalDateTime readingDate;

    /*
     * Similar as with LidarReading - treat this as an enormous string split by newline characters and commas:
     * PostgreSQL - text - https://www.postgresqltutorial.com/postgresql-char-varchar-text/ https://dba.stackexchange.com/questions/189876/size-limit-of-character-varying-postgresql,
     * H2 - can handle maximally 1048576 characters in dev mode - http://www.h2database.com/html/datatypes.html
     * and after receiving, you can split() the received string to a na List<PointCloudSinglePoint>
     * */
    private String pointCloudReading;
}
