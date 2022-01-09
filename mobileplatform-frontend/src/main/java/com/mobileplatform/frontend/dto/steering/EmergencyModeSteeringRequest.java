package com.mobileplatform.frontend.dto.steering;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class EmergencyModeSteeringRequest implements Serializable {

    private int ea;
    private int vid;
    private int mgc;
}
