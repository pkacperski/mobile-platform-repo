package com.mobileplatform.frontend.controller.action;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mobileplatform.frontend.controller.action.creation.Actions;
import com.mobileplatform.frontend.controller.api.RestHandler;
import com.mobileplatform.frontend.dto.*;
import com.mobileplatform.frontend.dto.steering.*;
import com.mobileplatform.frontend.form.MainForm;
import lombok.Getter;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Log
public class MainFormActions implements Actions {
    private static MainFormActions mainFormActions;

    private @Getter MainForm mainForm;
    private Gson gson;

    private RestHandler<DiagnosticDataDto> diagnosticDataDtoRestHandler;
    private RestHandler<DrivingModeDataDto> drivingModeDataDtoRestHandler;
    private RestHandler<DrivingModeSteeringResponse> drivingModeSteeringResponseRestHandler;
    private RestHandler<EmergencyModeDataDto> emergencyModeDataDtoRestHandler;
    private RestHandler<EmergencyModeSteeringResponse> emergencyModeSteeringResponseRestHandler;
    private RestHandler<EncoderReadingDto> encoderReadingDtoRestHandler;
    private RestHandler<ImuReadingDto> imuReadingDtoRestHandler;
    private RestHandler<LidarReadingDto> lidarReadingDtoRestHandler;
    private RestHandler<LocationDto> locationDtoRestHandler;
    private RestHandler<PointCloudDto> pointCloudDtoRestHandler;
    private RestHandler<VehicleDto> vehicleDtoRestHandler;
    private RestHandler<VehicleConnectResponse> vehicleConnectResponseRestHandler;
    private RestHandler<VehicleActivateConnectionResponse> vehicleActivateConnectionResponseRestHandler;

    final String VEHICLE_PATH = "/vehicle";
    final String DIAGNOSTIC_DATA_NEWEST_PATH = "/diagnostic-data/newest";
    final String DRIVING_MODE_DATA_PATH = "/driving-mode-data";
    final String EMERGENCY_MODE_DATA_PATH = "/emergency-mode-data";
    final String ENCODER_READING_NEWEST_PATH = "/encoder-reading/newest";
    final String IMU_READING_NEWEST_PATH = "/imu-reading/newest";
    final String LIDAR_READING_NEWEST_PATH = "/lidar-reading/newest";
    final String LOCATION_NEWEST_PATH = "/location/newest";
    final String POINT_CLOUD_NEWEST_PATH = "/point-cloud/newest";
    final String ID_1 = "1";
    final String NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE = "JSONArray text must start with '['";
    final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
    final int DRIVING_MODE_AUTONOMOUS_API_CONST = 1;
    final int DRIVING_MODE_MANUAL_API_CONST = 2;
    final int EMERGENCY_MODE_STOP_API_CONST = 1;
    final int EMERGENCY_MODE_ABORT_API_CONST = 2;

    private MainFormActions() {}

    public static synchronized MainFormActions getInstance() {
        if(mainFormActions == null)
            mainFormActions = new MainFormActions();
        return mainFormActions;
    }

    @Override
    public void control() {
        initialize();
    }

    @Override
    public void initialize() {
        mainForm = new MainForm();
        mainForm.getFrame().setVisible(true);
        gson = Converters.registerLocalDateTime(new GsonBuilder()).create();

        diagnosticDataDtoRestHandler = new RestHandler<>(DiagnosticDataDto.class);
        drivingModeDataDtoRestHandler = new RestHandler<>(DrivingModeDataDto.class);
        drivingModeSteeringResponseRestHandler = new RestHandler<>(DrivingModeSteeringResponse.class);
        emergencyModeDataDtoRestHandler = new RestHandler<>(EmergencyModeDataDto.class);
        emergencyModeSteeringResponseRestHandler = new RestHandler<>(EmergencyModeSteeringResponse.class);
        encoderReadingDtoRestHandler = new RestHandler<>(EncoderReadingDto.class);
        imuReadingDtoRestHandler = new RestHandler<>(ImuReadingDto.class);
        lidarReadingDtoRestHandler = new RestHandler<>(LidarReadingDto.class);
        locationDtoRestHandler = new RestHandler<>(LocationDto.class);
        pointCloudDtoRestHandler = new RestHandler<>(PointCloudDto.class);
        vehicleDtoRestHandler = new RestHandler<>(VehicleDto.class);
        vehicleConnectResponseRestHandler = new RestHandler<>(VehicleConnectResponse.class);
        vehicleActivateConnectionResponseRestHandler = new RestHandler<>(VehicleActivateConnectionResponse.class);

        mainForm.getBtnConnectVehicle().addActionListener(e -> sendConnectVehicleSignal(mainForm.getTxtVehicleIp().getText(), mainForm.getTxtVehicleName().getText()));
        mainForm.getBtnEmergencyStop().addActionListener(e -> sendEmergencySignal(EmergencyMode.STOP));
        mainForm.getBtnEmergencyAbort().addActionListener(e -> sendEmergencySignal(EmergencyMode.ABORT_MISSION_AND_RETURN));
        mainForm.getBtnAutonomousDrivingMode().addActionListener(e -> sendDrivingModeSignal(DrivingMode.AUTONOMOUS));
        mainForm.getBtnManualSteeringMode().addActionListener(e -> sendDrivingModeSignal(DrivingMode.MANUAL_STEERING));
        mainForm.getBtnFetchData().addActionListener(e -> refreshDataInMainPanel());
    }

    private void sendConnectVehicleSignal(String vehicleIp, String vehicleName) {
        VehicleDto vehicleDto = VehicleDto.builder()
                .ipAddress(vehicleIp)
                .name(vehicleName)
                .connectionDate(LocalDateTime.now())
                .build();
        try {
            VehicleDto vehicleDtoResponse = vehicleDtoRestHandler.performPost(VEHICLE_PATH, gson.toJson(vehicleDto), APPLICATION_JSON_CONTENT_TYPE);
            if(vehicleDtoResponse.getId() != null) {
                VehicleConnectRequest vehicleConnectRequest = VehicleConnectRequest.builder()
                        .addr("localhost") // TODO - adres IP serwera do ktorego wysylac dane w sieci lokalnej
                        .port(8080) // TODO - port serwera do odbioru danych - zawsze 8080?
                        .vid(vehicleDtoResponse.getId().intValue())
                        .mgc(60949)
                        .build();
                vehicleConnectResponseRestHandler.performPost(vehicleIp + "/connect", gson.toJson(vehicleConnectRequest), APPLICATION_JSON_CONTENT_TYPE);

                VehicleActivateConnectionRequest vehicleActivateConnectionRequest = VehicleActivateConnectionRequest.builder()
                        .activ(true)
                        .vid(vehicleDtoResponse.getId().intValue())
                        .mgc(23589)
                        .build();
                // TODO - change MPcomms like in https://github.com/Szewoj/MPcomms/issues/1 OR change below to PUT Http method instead of POST
                VehicleActivateConnectionResponse v = vehicleActivateConnectionResponseRestHandler.performPost(vehicleIp + "/connect/activate", gson.toJson(vehicleActivateConnectionRequest), APPLICATION_JSON_CONTENT_TYPE);
                // TODO - if(v.activ != true) ... -> obsluga bledu
                // Update the labels in the view:
                mainForm.getLblVehicleId().setText("Vehicle ID: " + vehicleDtoResponse.getId());
                mainForm.getLblVehicleIp().setText("Vehicle IP address: " + vehicleIp);
                mainForm.getLblVehicleName().setText("Vehicle name: " + vehicleDtoResponse.getName());
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void sendEmergencySignal(EmergencyMode emergencyMode) {
        Long storedVehicleId = Long.parseLong(mainForm.getLblVehicleId().getText().substring(mainForm.getLblVehicleId().getText().indexOf(':') + 2)); /* TODO vehicleId drugiego pojazdu */
        String storedVehicleIp = mainForm.getLblVehicleIp().getText().substring(mainForm.getLblVehicleIp().getText().indexOf(':') + 2); /* TODO vehicleIp drugiego pojazdu */
        if(storedVehicleId < 0 || storedVehicleIp.equals(""))
            return;
        EmergencyModeDataDto emergencyModeDataDto = EmergencyModeDataDto.builder()
                .vehicleId(storedVehicleId)
                .signalSendingDate(LocalDateTime.now())
                .emergencyMode(emergencyMode)
                .build();
        EmergencyModeSteeringRequest emergencyModeSteeringRequest = EmergencyModeSteeringRequest.builder()
                .ea(emergencyMode.equals(EmergencyMode.STOP) ? EMERGENCY_MODE_STOP_API_CONST : EMERGENCY_MODE_ABORT_API_CONST)
                .vid(storedVehicleId.intValue())
                .mgc(31460)
                .build();
        try {
            emergencyModeDataDtoRestHandler.performPost(EMERGENCY_MODE_DATA_PATH, gson.toJson(emergencyModeDataDto), APPLICATION_JSON_CONTENT_TYPE);
            emergencyModeSteeringResponseRestHandler.performPost(storedVehicleIp + "/emergency", gson.toJson(emergencyModeSteeringRequest), APPLICATION_JSON_CONTENT_TYPE);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void sendDrivingModeSignal(DrivingMode drivingMode) {
        Long storedVehicleId = Long.parseLong(mainForm.getLblVehicleId().getText().substring(mainForm.getLblVehicleId().getText().indexOf(':') + 2)); /* TODO vehicleId drugiego pojazdu */
        String storedVehicleIp = mainForm.getLblVehicleIp().getText().substring(mainForm.getLblVehicleIp().getText().indexOf(':') + 2); /* TODO vehicleIp drugiego pojazdu */
        if(storedVehicleId < 0 || storedVehicleIp.equals(""))
            return;
        DrivingModeDataDto drivingModeDataDto = DrivingModeDataDto.builder()
                .vehicleId(storedVehicleId)
                .signalSendingDate(LocalDateTime.now())
                .drivingMode(drivingMode)
                .build();
        DrivingModeSteeringRequest drivingModeSteeringRequest = DrivingModeSteeringRequest.builder()
                .mode(drivingMode.equals(DrivingMode.AUTONOMOUS) ? DRIVING_MODE_AUTONOMOUS_API_CONST : DRIVING_MODE_MANUAL_API_CONST)
                .vid(storedVehicleId.intValue())
                .mgc(32129)
                .build();
        try {
            drivingModeDataDtoRestHandler.performPost(DRIVING_MODE_DATA_PATH, gson.toJson(drivingModeDataDto), APPLICATION_JSON_CONTENT_TYPE);
            drivingModeSteeringResponseRestHandler.performPost(storedVehicleIp + "/mode", gson.toJson(drivingModeSteeringRequest), APPLICATION_JSON_CONTENT_TYPE);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    // TODO - reformat (duplikacja kodu!) + docelowo przycisk "Refresh data" niepotrzebny
    public void refreshDataInMainPanel() {
        VehicleDto vehicleDto = null;
        DiagnosticDataDto diagnosticDataDto = null;
        EncoderReadingDto encoderReadingDto = null;
        ImuReadingDto imuReadingDto = null;
        LidarReadingDto lidarReadingDto = null;
        LocationDto locationDto = null;
        PointCloudDto pointCloudDto = null;

        try {
            vehicleDto = vehicleDtoRestHandler.performGet(VEHICLE_PATH, ID_1);
        } catch (UnirestException exception) {
            // When no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException which is handled - hence only log other exceptions
            if(!exception.getMessage().contains(NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE)) {
                log.severe(exception.getMessage());
            }
        }

        try {
            diagnosticDataDto = diagnosticDataDtoRestHandler.performGet(DIAGNOSTIC_DATA_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            // When no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException which is handled - hence only log other exceptions
            if(!exception.getMessage().contains(NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE)) {
                log.severe(exception.getMessage());
            }
        }

        try {
            encoderReadingDto = encoderReadingDtoRestHandler.performGet(ENCODER_READING_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            // When no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException which is handled - hence only log other exceptions
            if(!exception.getMessage().contains(NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE)) {
                log.severe(exception.getMessage());
            }
        }

        try {
            imuReadingDto = imuReadingDtoRestHandler.performGet(IMU_READING_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            // When no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException which is handled - hence only log other exceptions
            if(!exception.getMessage().contains(NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE)) {
                log.severe(exception.getMessage());
            }
        }

        try {
            lidarReadingDto = lidarReadingDtoRestHandler.performGet(LIDAR_READING_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            // When no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException which is handled - hence only log other exceptions
            if(!exception.getMessage().contains(NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE)) {
                log.severe(exception.getMessage());
            }
        }

        try {
            locationDto = locationDtoRestHandler.performGet(LOCATION_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            // When no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException which is handled - hence only log other exceptions
            if(!exception.getMessage().contains(NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE)) {
                log.severe(exception.getMessage());
            }
        }

        try {
            pointCloudDto = pointCloudDtoRestHandler.performGet(POINT_CLOUD_NEWEST_PATH, ID_1);
        } catch (UnirestException exception) {
            // When no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException which is handled - hence only log other exceptions
            if(!exception.getMessage().contains(NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE)) {
                log.severe(exception.getMessage());
            }
        }

        mainForm.getLblVehicleName().setText(vehicleDto != null ? "Vehicle name: " + vehicleDto.getName() : "Vehicle not connected");
        mainForm.getLblVehicleIp().setText(vehicleDto != null ? "Vehicle IP address: " + vehicleDto.getIpAddress() : "Vehicle not connected");
        mainForm.getLblVehicleId().setText(vehicleDto != null ? "Vehicle ID: " + vehicleDto.getId() : "Vehicle not connected");
        mainForm.getLblDiagnosticData().setText(diagnosticDataDto != null ? "Battery status: " + diagnosticDataDto.getBatteryChargeStatus()
                + ", wheels turn measure: " + diagnosticDataDto.getWheelsTurnMeasure() : "No diagnostic data received");
        mainForm.getLblEncoderReading().setText(encoderReadingDto != null ? "Encoder reading: left front wheel: " + encoderReadingDto.getLeftFrontWheelSpeed()
                + "..." : "No encoder readings received");
        mainForm.getLblImuReading().setText(imuReadingDto != null ? "IMU reading: acceleration X: " + imuReadingDto.getAccelerationX() + " ..." : "No IMU readings received");
        mainForm.getLblLidarReading().setText(lidarReadingDto != null ? "Lidar reading: " + lidarReadingDto.getLidarDistancesReading() : "No lidar readings received");
        mainForm.getLblLocation().setText(locationDto != null ? "Location: real X: " + locationDto.getRealXCoordinate()
                + ", real Y: " + locationDto.getRealYCoordinate() : "No location data received");
        mainForm.getLblPointCloudReading().setText(pointCloudDto != null ? "Point cloud reading: " + pointCloudDto.getPointCloudReading() : "No point cloud reading received");
    }
}
