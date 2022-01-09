package com.mobileplatform.frontend.dto.steering;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class VehicleActivateConnectionResponse implements Serializable {

    private int vid;
    private boolean activ;
}
