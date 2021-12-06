package com.mobileplatform.frontend.controller.action;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mobileplatform.frontend.controller.action.creation.Actions;
import com.mobileplatform.frontend.controller.api.RestHandler;
import com.mobileplatform.frontend.dto.*;
import com.mobileplatform.frontend.form.MainForm;
import lombok.extern.java.Log;

@Log
public class MainFormActions implements Actions {
    private static MainFormActions mainFormActions;

    private MainForm mainForm;
    private RestHandler<DiagnosticDataDto> diagnosticDataDtoRestHandler;
    private RestHandler<EncoderReadingDto> encoderReadingDtoRestHandler;
    private RestHandler<ImuReadingDto> imuReadingDtoRestHandler;
    private RestHandler<LidarReadingDto> lidarReadingDtoRestHandler;
    private RestHandler<LocationDto> locationDtoRestHandler;
    private RestHandler<PointCloudDto> pointCloudDtoRestHandler;
    private RestHandler<VehicleDto> vehicleDtoRestHandler;

    private MainFormActions() {}

    public static synchronized MainFormActions getInstance() {
        if(mainFormActions == null)
            mainFormActions = new MainFormActions();
        return mainFormActions;
    }

    @Override
    public void initialize() {
        mainForm = new MainForm();
        mainForm.getFrame().setVisible(true);

        diagnosticDataDtoRestHandler = new RestHandler<>(DiagnosticDataDto.class);
        encoderReadingDtoRestHandler = new RestHandler<>(EncoderReadingDto.class);
        imuReadingDtoRestHandler = new RestHandler<>(ImuReadingDto.class);
        lidarReadingDtoRestHandler = new RestHandler<>(LidarReadingDto.class);
        locationDtoRestHandler = new RestHandler<>(LocationDto.class);
        pointCloudDtoRestHandler = new RestHandler<>(PointCloudDto.class);
        vehicleDtoRestHandler = new RestHandler<>(VehicleDto.class);

        mainForm.getBtnRestCall().addActionListener(e -> onClickBtnRestCall());
    }

    @Override
    public void control() {
        initialize();
    }

    private void onClickBtnRestCall() {
        final String VEHICLE_PATH = "/vehicle";
        final String DIAGNOSTIC_DATA_NEWEST_PATH = "/diagnostic-data/newest";
        final String ENCODER_READING_NEWEST_PATH = "/encoder-reading/newest";
        final String IMU_READING_NEWEST_PATH = "/imu-reading/newest";
        final String LIDAR_READING_NEWEST_PATH = "/lidar-reading/newest";
        final String LOCATION_NEWEST_PATH = "/location/newest";
        final String POINT_CLOUD_NEWEST_PATH = "/point-cloud/newest";
        final String ID_1 = "1";

        VehicleDto vehicleDto;
        try {
            vehicleDto = vehicleDtoRestHandler.performGet(VEHICLE_PATH, ID_1);
        } catch (UnirestException exception) {
            log.warning(exception.getMessage());
            vehicleDto = null; // when no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException
        }

        DiagnosticDataDto diagnosticDataDto;
        try {
            diagnosticDataDto = diagnosticDataDtoRestHandler.performGet(DIAGNOSTIC_DATA_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            log.warning(exception.getMessage());
            diagnosticDataDto = null; // when no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException
        }

        EncoderReadingDto encoderReadingDto;
        try {
            encoderReadingDto = encoderReadingDtoRestHandler.performGet(ENCODER_READING_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            log.warning(exception.getMessage());
            encoderReadingDto = null; // when no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException
        }

        ImuReadingDto imuReadingDto;
        try {
            imuReadingDto = imuReadingDtoRestHandler.performGet(IMU_READING_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            log.warning(exception.getMessage());
            imuReadingDto = null; // when no object is found under specified URL, Unirest cannot parse the response as JSON, throws a UnirestException and null is returned
        }

        LidarReadingDto lidarReadingDto;
        try {
            lidarReadingDto = lidarReadingDtoRestHandler.performGet(LIDAR_READING_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            log.warning(exception.getMessage());
            lidarReadingDto = null; // when no object is found under specified URL, Unirest cannot parse the response as JSON, throws a UnirestException and null is returned
        }

        LocationDto locationDto;
        try {
            locationDto = locationDtoRestHandler.performGet(LOCATION_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            log.warning(exception.getMessage());
            locationDto = null; // when no object is found under specified URL, Unirest cannot parse the response as JSON, throws a UnirestException and null is returned
        }

        PointCloudDto pointCloudDto;
        try {
            pointCloudDto = pointCloudDtoRestHandler.performGet(POINT_CLOUD_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            log.warning(exception.getMessage());
            pointCloudDto = null; // when no object is found under specified URL, Unirest cannot parse the response as JSON, throws a UnirestException and null is returned
        }

        mainForm.getLblVehicleName().setText(vehicleDto != null ? "Vehicle name: " + vehicleDto.getVehicleName() : "No vehicle found");
        mainForm.getLblDiagnosticData().setText(diagnosticDataDto != null ? diagnosticDataDto.toString() : "No diagnostic data received");
        mainForm.getLblEncoderReading().setText(encoderReadingDto != null ? encoderReadingDto.toString() : "No encoder readings received");
        mainForm.getLblImuReading().setText(imuReadingDto != null ? imuReadingDto.toString() : "No IMU readings received");
        mainForm.getLblLidarReading().setText(lidarReadingDto != null ? lidarReadingDto.toString() : "No lidar readings received");
        mainForm.getLblLocation().setText(locationDto != null ? locationDto.toString() : "No location data received");
        mainForm.getLblPointCloudReading().setText(pointCloudDto != null ? pointCloudDto.toString() : "No point cloud reading received");
    }
}
