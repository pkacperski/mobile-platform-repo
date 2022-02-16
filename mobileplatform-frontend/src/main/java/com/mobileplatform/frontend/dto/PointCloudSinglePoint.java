package com.mobileplatform.frontend.dto;

import lombok.Data;

@Data
public class PointCloudSinglePoint {

    private Float xCoordinate;
    private Float yCoordinate;
    private Float zCoordinate;

    private Float rChannelValue;
    private Float gChannelValue;
    private Float bChannelValue;
}
