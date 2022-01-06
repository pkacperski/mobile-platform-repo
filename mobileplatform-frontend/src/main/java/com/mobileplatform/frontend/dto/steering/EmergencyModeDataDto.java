package com.mobileplatform.frontend.dto.steering;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class EmergencyModeDataDto implements Serializable {

    private Long id;
    private Long vehicleId;
    private LocalDateTime signalSendingDate;
    private EmergencyMode emergencyMode;
}
