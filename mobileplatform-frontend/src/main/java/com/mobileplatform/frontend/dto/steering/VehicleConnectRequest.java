package com.mobileplatform.frontend.dto.steering;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class VehicleConnectRequest implements Serializable {

    private String addr;
    private int port;
    private int vid;
    private int mgc;
}
