package com.mobileplatform.frontend.dto.steering;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class VehicleActivateConnectionRequest implements Serializable {

    private boolean activ;
    private int vid;
    private int mgc;
}
