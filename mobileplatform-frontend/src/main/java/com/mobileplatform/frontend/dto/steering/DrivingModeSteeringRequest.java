package com.mobileplatform.frontend.dto.steering;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DrivingModeSteeringRequest implements Serializable {

    private int mode;
    private int vid;
    private int mgc;
}
