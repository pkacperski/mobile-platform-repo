package com.mobileplatform.frontend.dto.steering;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class VehicleConnectResponse implements Serializable {

    private int vid;
}
