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
import com.mobileplatform.frontend.form.VehicleLocationPane;
import com.mobileplatform.frontend.opencv.VideoReceiveHandler;
import com.mobileplatform.frontend.websockets.TelemetryClient;
import lombok.Getter;
import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

import static com.mobileplatform.frontend.MobileplatformFrontend.*;
import static com.mobileplatform.frontend.opencv.VideoReceiveHandler.createMockLidarAndPointCloudStreamClients;
import static com.mobileplatform.frontend.opencv.VideoReceiveHandler.disableMockLidarAndPointCloudStreamClients;

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
    private RestHandler<LocationDto[]> locationDtoListRestHandler;
    private RestHandler<PointCloudDto> pointCloudDtoRestHandler;
    private RestHandler<VehicleDto> vehicleDtoRestHandler;
    private RestHandler<VehicleConnectResponse> vehicleConnectResponseRestHandler;

    final String VEHICLE_PATH = "/vehicle";
    final String DIAGNOSTIC_DATA_NEWEST_PATH = "/diagnostic-data/newest";
    final String DRIVING_MODE_DATA_PATH = "/driving-mode-data";
    final String EMERGENCY_MODE_DATA_PATH = "/emergency-mode-data";
    final String ENCODER_READING_NEWEST_PATH = "/encoder-reading/newest";
    final String IMU_READING_NEWEST_PATH = "/imu-reading/newest";
    final String LIDAR_READING_NEWEST_PATH = "/lidar-reading/newest";
    final String LOCATION_NEWEST_PATH = "/location/newest";
    final String POINT_CLOUD_NEWEST_PATH = "/point-cloud/newest";
    final String NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE = "JSONArray text must start with '['";
    final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
    final int DRIVING_MODE_AUTONOMOUS_API_CONST = 1;
    final int DRIVING_MODE_MANUAL_API_CONST = 2;
    final int EMERGENCY_MODE_STOP_API_CONST = 1;
    final int EMERGENCY_MODE_ABORT_API_CONST = 2;
    final int VEHICLE_1 = 1;
    final int VEHICLE_2 = 2;
    final int STREAM_1 = 1;
    final int STREAM_2 = 2;
    final int STREAM_3 = 3;
    final int STREAM_4 = STREAMS_PER_VEHICLE_COUNT + 1;
    final int STREAM_5 = STREAMS_PER_VEHICLE_COUNT + 2;
    final int STREAM_6 = STREAMS_PER_VEHICLE_COUNT + 3;

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
        locationDtoListRestHandler = new RestHandler<>(LocationDto[].class);
        pointCloudDtoRestHandler = new RestHandler<>(PointCloudDto.class);
        vehicleDtoRestHandler = new RestHandler<>(VehicleDto.class);
        vehicleConnectResponseRestHandler = new RestHandler<>(VehicleConnectResponse.class);

        mainForm.getBtnConnectVehicle().addActionListener(e -> sendConnectVehicleSignal(VEHICLE_1));
        mainForm.getBtnDisconnectVehicle().addActionListener(e -> sendDisconnectVehicleSignal(VEHICLE_1));
        mainForm.getBtnEmergencyStop().addActionListener(e -> sendEmergencySignal(EmergencyMode.STOP, VEHICLE_1));
        mainForm.getBtnEmergencyAbort().addActionListener(e -> sendEmergencySignal(EmergencyMode.ABORT_MISSION_AND_RETURN, VEHICLE_1));
        mainForm.getBtnAutonomousDrivingMode().addActionListener(e -> sendDrivingModeSignal(DrivingMode.AUTONOMOUS, VEHICLE_1));
        mainForm.getBtnManualSteeringMode().addActionListener(e -> sendDrivingModeSignal(DrivingMode.MANUAL_STEERING, VEHICLE_1));
        mainForm.getBtnFetchData().addActionListener(e -> refreshDataInMainPanel(VEHICLE_1));
        mainForm.getBtnStream1().addActionListener(e -> sendChangeActiveStreamSignal(VEHICLE_1, STREAM_1));
        mainForm.getBtnStream2().addActionListener(e -> sendChangeActiveStreamSignal(VEHICLE_1, STREAM_2));
        mainForm.getBtnStream3().addActionListener(e -> sendChangeActiveStreamSignal(VEHICLE_1, STREAM_3));
        mainForm.getBtnShowLocationHistory().addActionListener(e -> createFrameWithLocationHistory(VEHICLE_1));
        mainForm.getBtnShowLidarOccupancyMap().addActionListener(e -> createFrameWithLidarOccupancyMap());
        mainForm.getBtnShowPointCloud().addActionListener(e -> createFrameWithPointCloud());
        mainForm.getBtnOpenVehicle2View().addActionListener(e -> createFrameForVehicle2());
        mainForm.getBtnOpenAllDataView().addActionListener(e -> createFrameWithAllDataView());

        // TODO - dodac docelowe akcje dla pojazdu 2
        mainForm.getBtnConnectVehicle2().addActionListener(e -> sendConnectVehicleSignal(VEHICLE_2));
        mainForm.getBtnDisconnectVehicle2().addActionListener(e -> sendDisconnectVehicleSignal(VEHICLE_2));
        mainForm.getBtnEmergencyStopVehicle2().addActionListener(e -> sendEmergencySignal(EmergencyMode.STOP, VEHICLE_2));
        mainForm.getBtnEmergencyAbortVehicle2().addActionListener(e -> sendEmergencySignal(EmergencyMode.ABORT_MISSION_AND_RETURN, VEHICLE_2));
        mainForm.getBtnAutonomousDrivingModeVehicle2().addActionListener(e -> sendDrivingModeSignal(DrivingMode.AUTONOMOUS, VEHICLE_2));
        mainForm.getBtnManualSteeringModeVehicle2().addActionListener(e -> sendDrivingModeSignal(DrivingMode.MANUAL_STEERING, VEHICLE_2));
        mainForm.getBtnFetchDataVehicle2().addActionListener(e -> refreshDataInMainPanel(VEHICLE_2));
        mainForm.getBtnStream1Vehicle2().addActionListener(e -> sendChangeActiveStreamSignal(VEHICLE_2, STREAM_4));
        mainForm.getBtnStream2Vehicle2().addActionListener(e -> sendChangeActiveStreamSignal(VEHICLE_2, STREAM_5));
        mainForm.getBtnStream3Vehicle2().addActionListener(e -> sendChangeActiveStreamSignal(VEHICLE_2, STREAM_6));

        mainForm.getProgressBarBatteryStatus().setValue(0);
        mainForm.getProgressBarWheelsTurnLeft().setValue(0);
        mainForm.getProgressBarWheelsTurnRight().setValue(0);
        mainForm.getProgressBarCamerasTurnLeft().setValue(0);
        mainForm.getProgressBarCamerasTurnRight().setValue(0);
    }

    private void sendConnectVehicleSignal(int whichVehicle) {

        String vehicleIp = (whichVehicle == 1) ? mainForm.getTxtVehicleIp().getText() : mainForm.getTxtVehicleIpVehicle2().getText();
        String vehicleName = (whichVehicle == 1) ? mainForm.getTxtVehicleName().getText() : mainForm.getTxtVehicleNameVehicle2().getText();
        VehicleDto vehicleDto = VehicleDto.builder()
                .ipAddress(vehicleIp)
                .name(vehicleName + "$" + whichVehicle)
                .connectionDate(LocalDateTime.now())
                .connectionStatus(VehicleConnectionStatus.CONNECTED)
                .build();
        try {
            VehicleDto vehicleDtoResponse = vehicleDtoRestHandler.performPost(VEHICLE_PATH, gson.toJson(vehicleDto), APPLICATION_JSON_CONTENT_TYPE);
            if(vehicleDtoResponse.getId() != null) {
                VehicleConnectRequest vehicleConnectRequest = VehicleConnectRequest.builder()
                        .addr(IS_TEST_ENV ? TELEMETRY_API_SERVER_IP_TEST : TELEMETRY_API_SERVER_IP_PROD)
                        .port(TELEMETRY_API_PORT_NUMBER)
                        .vid(vehicleDtoResponse.getId().intValue())
                        .mgc(60949)
                        .build();
                vehicleConnectResponseRestHandler.performPost(vehicleIp + "/connect", gson.toJson(vehicleConnectRequest), APPLICATION_JSON_CONTENT_TYPE);
                if(IS_TEST_ENV && IS_TEST_LIDAR_AND_PC_STREAMING)
                    createMockLidarAndPointCloudStreamClients();
                setLabelsAfterConnect(whichVehicle, vehicleDtoResponse.getId(), vehicleIp, vehicleName);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void sendDisconnectVehicleSignal(int whichVehicle) {

        int storedVehicleId = (int) getVehicleId(whichVehicle);
        String storedVehicleIp = getVehicleIp(whichVehicle);
        String storedVehicleName = getVehicleName(whichVehicle);
        if(storedVehicleId < 0 || storedVehicleIp.equals(""))
            return;
        VehicleDto vehicleDto = VehicleDto.builder()
                .ipAddress(storedVehicleIp)
                .name(storedVehicleName + "$" + whichVehicle)
                .connectionDate(LocalDateTime.now())
                .connectionStatus(VehicleConnectionStatus.DISCONNECTED)
                .build();
        VehicleConnectRequest vehicleDisconnectRequest = VehicleConnectRequest.builder() // dopoki endpoint DELETE /connect ma takie samo body jak POST /connect, nie trzeba tworzyc nowego Dto na obsluge requesta ani nowego RestHandlera
                .addr(IS_TEST_ENV ? TELEMETRY_API_SERVER_IP_TEST : TELEMETRY_API_SERVER_IP_PROD)
                .port(TELEMETRY_API_PORT_NUMBER)
                .vid(storedVehicleId)
                .mgc(15061)
                .build();

        try {
            vehicleDtoRestHandler.performPost(VEHICLE_PATH, gson.toJson(vehicleDto), APPLICATION_JSON_CONTENT_TYPE);
            VehicleConnectResponse vehicleConnectResponse = vehicleConnectResponseRestHandler.performDelete(storedVehicleIp + "/connect", gson.toJson(vehicleDisconnectRequest), APPLICATION_JSON_CONTENT_TYPE);
            if(IS_TEST_ENV && IS_TEST_LIDAR_AND_PC_STREAMING)
                disableMockLidarAndPointCloudStreamClients();
            if(vehicleConnectResponse.getVid() == storedVehicleId) { // TODO - spr. dlaczego kiedys przy jakiejs probie strzal pod API sterujace nie zwracal prawidlowego id pojazdu tylko vid=0 (-> performDelete)
                setLabelsAfterDisconnect(whichVehicle);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void setLabelsAfterConnect(int whichVehicle, Long vehicleId, String vehicleIp, String vehicleName) {

        if (whichVehicle == VEHICLE_1) {
            mainForm.getLblVehicleId().setText("Vehicle ID: " + vehicleId);
            mainForm.getLblVehicleIp().setText("Vehicle IP: " + vehicleIp);
            mainForm.getLblVehicleName().setText("Vehicle name: " + vehicleName);
            mainForm.getLblVehicleNameAllData().setText(vehicleName);
            mainForm.getLblVehicleIpAllData().setText(vehicleIp);
            mainForm.getLblEmergencyModeAllData().setText("Not set");
            mainForm.getLblDrivingModeAllData().setText("Not set");
            mainForm.getBtnConnectVehicle().setEnabled(false);
            mainForm.getBtnDisconnectVehicle().setEnabled(true);
            mainForm.getBtnAutonomousDrivingMode().setEnabled(true);
            mainForm.getBtnEmergencyStop().setEnabled(true);
            mainForm.getBtnEmergencyAbort().setEnabled(true);
            mainForm.getBtnManualSteeringMode().setEnabled(true);
            mainForm.getBtnShowLocationHistory().setEnabled(true);
            mainForm.getBtnShowLidarOccupancyMap().setEnabled(true);
            mainForm.getBtnShowPointCloud().setEnabled(true);
        } else if (whichVehicle == VEHICLE_2) {
            mainForm.getLblVehicleIdVehicle2().setText("Vehicle ID: " + vehicleId);
            mainForm.getLblVehicleIpVehicle2().setText("Vehicle IP: " + vehicleIp);
            mainForm.getLblVehicleNameVehicle2().setText("Vehicle name: " + vehicleName);
            mainForm.getBtnConnectVehicle2().setEnabled(false);
            mainForm.getBtnDisconnectVehicle2().setEnabled(true);
            mainForm.getBtnAutonomousDrivingModeVehicle2().setEnabled(true);
            mainForm.getBtnEmergencyStopVehicle2().setEnabled(true);
            mainForm.getBtnEmergencyAbortVehicle2().setEnabled(true);
            mainForm.getBtnManualSteeringModeVehicle2().setEnabled(true);
        }
    }

    private void setLabelsAfterDisconnect(int whichVehicle) {

        if(whichVehicle == VEHICLE_1) {
            mainForm.getBtnConnectVehicle().setEnabled(true);
            mainForm.getBtnDisconnectVehicle().setEnabled(false);
            mainForm.getBtnAutonomousDrivingMode().setEnabled(false);
            mainForm.getBtnEmergencyStop().setEnabled(false);
            mainForm.getBtnEmergencyAbort().setEnabled(false);
            mainForm.getBtnManualSteeringMode().setEnabled(false);
            mainForm.getBtnShowLocationHistory().setEnabled(false);
            mainForm.getBtnShowLidarOccupancyMap().setEnabled(false);
            mainForm.getBtnShowPointCloud().setEnabled(false);
            mainForm.getLblVehicleId().setText("Vehicle not connected");
            mainForm.getLblVehicleIp().setText("Vehicle not connected");
            mainForm.getLblVehicleName().setText("Vehicle not connected");
            mainForm.getLblAccelerometerReading().setText("No IMU readings received");
            mainForm.getLblVideoStream().setIcon(new ImageIcon());
            mainForm.getProgressBarWheelsTurnLeft().setValue(0);
            mainForm.getProgressBarWheelsTurnRight().setValue(0);
            mainForm.getProgressBarCamerasTurnLeft().setValue(0);
            mainForm.getProgressBarCamerasTurnRight().setValue(0);
            mainForm.getProgressBarBatteryStatus().setValue(0);
            mainForm.getProgressBarLeftFrontWheelSpeed().setValue(0);
            mainForm.getProgressBarRightFrontWheelSpeed().setValue(0);
            mainForm.getProgressBarLeftRearWheelSpeed().setValue(0);
            mainForm.getProgressBarRightRearWheelSpeed().setValue(0);
            mainForm.getLblAccelerometerReading().setText("");
            mainForm.getLblGyroReading().setText("");
            mainForm.getLblMagnetometerReading().setText("");
        }
        else if(whichVehicle == VEHICLE_2) {
            mainForm.getBtnConnectVehicle2().setEnabled(true);
            mainForm.getBtnDisconnectVehicle2().setEnabled(false);
            mainForm.getBtnAutonomousDrivingModeVehicle2().setEnabled(false);
            mainForm.getBtnEmergencyStopVehicle2().setEnabled(false);
            mainForm.getBtnEmergencyAbortVehicle2().setEnabled(false);
            mainForm.getBtnManualSteeringModeVehicle2().setEnabled(false);
            mainForm.getLblVehicleIdVehicle2().setText("Vehicle not connected");
            mainForm.getLblVehicleIpVehicle2().setText("Vehicle not connected");
            mainForm.getLblVehicleNameVehicle2().setText("Vehicle not connected");
            mainForm.getLblPointCloudReadingVehicle2().setText("No point cloud reading received");
            mainForm.getLblLocationVehicle2().setText("No location data received");
            mainForm.getLblLidarReadingVehicle2().setText("No lidar readings received");
            mainForm.getLblImuReadingVehicle2().setText("No IMU readings received");
            mainForm.getLblEncoderReadingVehicle2().setText("No encoder readings received");
            mainForm.getLblDiagnosticDataVehicle2().setText("No diagnostic data received");
            mainForm.getLblVideoStreamVehicle2().setIcon(new ImageIcon());
            // TODO - reset battery charge status, wheels' turn, etc.
        }
    }

    private void sendEmergencySignal(EmergencyMode emergencyMode, int whichVehicle) {

        long storedVehicleId = getVehicleId(whichVehicle);
        String storedVehicleIp = getVehicleIp(whichVehicle);
        if(storedVehicleId < 0 || storedVehicleIp.equals(""))
            return;
        EmergencyModeDataDto emergencyModeDataDto = EmergencyModeDataDto.builder()
                .vehicleId(storedVehicleId)
                .signalSendingDate(LocalDateTime.now())
                .emergencyMode(emergencyMode)
                .build();
        EmergencyModeSteeringRequest emergencyModeSteeringRequest = EmergencyModeSteeringRequest.builder()
                .ea(emergencyMode.equals(EmergencyMode.STOP) ? EMERGENCY_MODE_STOP_API_CONST : EMERGENCY_MODE_ABORT_API_CONST)
                .vid((int) storedVehicleId)
                .mgc(31460)
                .build();
        try {
            emergencyModeDataDtoRestHandler.performPost(EMERGENCY_MODE_DATA_PATH, gson.toJson(emergencyModeDataDto), APPLICATION_JSON_CONTENT_TYPE);
            emergencyModeSteeringResponseRestHandler.performPost(storedVehicleIp + "/emergency", gson.toJson(emergencyModeSteeringRequest), APPLICATION_JSON_CONTENT_TYPE);
            mainForm.getLblCurrentEmergencyMode().setText("Current mode: " + (emergencyMode.equals(EmergencyMode.STOP) ? "STOP" : "abort mission"));
            mainForm.getLblCurrentEmergencyMode().setVisible(true);
            mainForm.getLblEmergencyModeAllData().setText(emergencyMode.equals(EmergencyMode.STOP) ? "STOP" : "abort mission");
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void sendDrivingModeSignal(DrivingMode drivingMode, int whichVehicle) {

        long storedVehicleId = getVehicleId(whichVehicle);
        String storedVehicleIp = getVehicleIp(whichVehicle);
        if(storedVehicleId < 0 || storedVehicleIp.equals(""))
            return;
        DrivingModeDataDto drivingModeDataDto = DrivingModeDataDto.builder()
                .vehicleId(storedVehicleId)
                .signalSendingDate(LocalDateTime.now())
                .drivingMode(drivingMode)
                .build();
        DrivingModeSteeringRequest drivingModeSteeringRequest = DrivingModeSteeringRequest.builder()
                .mode(drivingMode.equals(DrivingMode.AUTONOMOUS) ? DRIVING_MODE_AUTONOMOUS_API_CONST : DRIVING_MODE_MANUAL_API_CONST)
                .vid((int) storedVehicleId)
                .mgc(32129)
                .build();
        try {
            drivingModeDataDtoRestHandler.performPost(DRIVING_MODE_DATA_PATH, gson.toJson(drivingModeDataDto), APPLICATION_JSON_CONTENT_TYPE);
            drivingModeSteeringResponseRestHandler.performPost(storedVehicleIp + "/mode", gson.toJson(drivingModeSteeringRequest), APPLICATION_JSON_CONTENT_TYPE);
            mainForm.getLblCurrentDrivingMode().setText("Current mode: " + (drivingMode.equals(DrivingMode.AUTONOMOUS) ? "autonomous" : "manual"));
            mainForm.getLblCurrentDrivingMode().setVisible(true);
            mainForm.getLblDrivingModeAllData().setText(drivingMode.equals(DrivingMode.AUTONOMOUS) ? "autonomous" : "manual");
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void sendChangeActiveStreamSignal(int whichVehicle, int whichStream) {
        TelemetryClient.getInstance().sendMessage("Change stream for vehicle: " + whichVehicle + " to stream " + whichStream + ".");
    }

    private void createFrameWithLocationHistory(int whichVehicle) {
        long storedVehicleId = getVehicleId(whichVehicle);
        if(storedVehicleId < 0)
            return;

        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Vehicle location (red - real, blue - SLAM)");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                // TODO - odczyt z listy trzymanej lokalnie zamiast strzalu pod API BE
                LocationDto[] locationDtos = locationDtoListRestHandler.performGetAbsolutePath("http://localhost:8080/location/" + storedVehicleId);
                frame.add(new VehicleLocationPane(locationDtos));
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        });
    }

    private void createFrameWithLidarOccupancyMap() {
        // only for testing & demonstration purposes
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Occupancy map from lidar");
            frame.setSize(360, 360);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageIcon imageIcon = VideoReceiveHandler.getLidarImageIcon();
            JLabel label = new JLabel();
            label.setIcon(imageIcon);
            frame.add(label);
            frame.setVisible(true);
        });
    }

    private void createFrameWithPointCloud() {
        // only for testing & demonstration purposes
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Point cloud visualisation");
            frame.setSize(520, 520); // ??? 600x600
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageIcon imageIcon = VideoReceiveHandler.getPcImageIcon();
            JLabel label = new JLabel();
            label.setIcon(imageIcon);
            frame.add(label);
            frame.setVisible(true);
        });
    }

    private void createFrameForVehicle2() {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Steering Cockpit - Vehicle 2");
            frame.setSize(1440, 810);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame.setContentPane(mainForm.getPanelVehicle2()); // TODO add functional components to panelVehicle2 - now only for testing & demonstration purposes
            frame.pack();
            frame.setVisible(true);
        });
    }

    private void createFrameWithAllDataView() { // TODO add functional components to panelVehicle2 - now only for testing & demonstration purposes
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("All data view - Vehicle 1");
            frame.setSize(1440, 980);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame.setContentPane(mainForm.getPanelAllDataVehicle1());
            frame.pack();
            frame.setVisible(true);
        });
    }

    private long getVehicleId(int whichVehicle) {
        return Long.parseLong((whichVehicle == 1)
                ? mainForm.getLblVehicleId().getText().substring(mainForm.getLblVehicleId().getText().indexOf(':') + 2)
                : mainForm.getLblVehicleIdVehicle2().getText().substring(mainForm.getLblVehicleIdVehicle2().getText().indexOf(':') + 2));
    }

    private String getVehicleIp(int whichVehicle) {
        return (whichVehicle == 1)
                ? mainForm.getLblVehicleIp().getText().substring(mainForm.getLblVehicleIp().getText().indexOf(':') + 2)
                : mainForm.getLblVehicleIpVehicle2().getText().substring(mainForm.getLblVehicleIpVehicle2().getText().indexOf(':') + 2);
    }

    private String getVehicleName(int whichVehicle) {
        return (whichVehicle == VEHICLE_1)
                ? mainForm.getLblVehicleName().getText().substring(mainForm.getLblVehicleName().getText().indexOf(':') + 2)
                : mainForm.getLblVehicleNameVehicle2().getText().substring(mainForm.getLblVehicleNameVehicle2().getText().indexOf(':') + 2);
    }

    // TODO - reformat (duplikacja kodu!) + docelowo przycisk "Refresh data" niepotrzebny
    public void refreshDataInMainPanel(int whichVehicle) {

        VehicleDto vehicleDto = null;
        DiagnosticDataDto diagnosticDataDto = null;
        EncoderReadingDto encoderReadingDto = null;
        ImuReadingDto imuReadingDto = null;
        LidarReadingDto lidarReadingDto = null;
        LocationDto locationDto = null;
        PointCloudDto pointCloudDto = null;

        String storedVehicleId = ((whichVehicle == 1)
                ? mainForm.getLblVehicleId().getText().substring(mainForm.getLblVehicleId().getText().indexOf(':') + 2)
                : mainForm.getLblVehicleIdVehicle2().getText().substring(mainForm.getLblVehicleIdVehicle2().getText().indexOf(':') + 2));

        try {
            vehicleDto = vehicleDtoRestHandler.performGet(VEHICLE_PATH, storedVehicleId);
            diagnosticDataDto = diagnosticDataDtoRestHandler.performGet(DIAGNOSTIC_DATA_NEWEST_PATH, storedVehicleId);
            encoderReadingDto = encoderReadingDtoRestHandler.performGet(ENCODER_READING_NEWEST_PATH, storedVehicleId);
            imuReadingDto = imuReadingDtoRestHandler.performGet(IMU_READING_NEWEST_PATH, storedVehicleId);
            lidarReadingDto = lidarReadingDtoRestHandler.performGet(LIDAR_READING_NEWEST_PATH, storedVehicleId);
            locationDto = locationDtoRestHandler.performGet(LOCATION_NEWEST_PATH, storedVehicleId);
            pointCloudDto = pointCloudDtoRestHandler.performGet(POINT_CLOUD_NEWEST_PATH, storedVehicleId);
        } catch (UnirestException exception) {
            // When no object is found under specified URL, Unirest cannot parse the response as JSON and throws a UnirestException which is handled - hence only log other exceptions
            if(!exception.getMessage().contains(NO_DATA_AT_SPECIFIED_LOCATION_ERROR_MESSAGE)) {
                log.severe(exception.getMessage());
            }
        }

        if(whichVehicle == 1) {
            mainForm.getLblVehicleName().setText(vehicleDto != null ? "Vehicle name: " + vehicleDto.getName() : "Vehicle not connected");
            mainForm.getLblVehicleIp().setText(vehicleDto != null ? "Vehicle IP address: " + vehicleDto.getIpAddress() : "Vehicle not connected");
            mainForm.getLblVehicleId().setText(vehicleDto != null ? "Vehicle ID: " + vehicleDto.getId() : "Vehicle not connected");
            mainForm.getLblAccelerometerReading().setText(imuReadingDto != null ? "IMU reading: acceleration X: " + imuReadingDto.getAccelerationX() + " ..." : "No IMU readings received");
        }
        else if(whichVehicle == 2) {
            mainForm.getLblVehicleNameVehicle2().setText(vehicleDto != null ? "Vehicle name: " + vehicleDto.getName() : "Vehicle not connected");
            mainForm.getLblVehicleIpVehicle2().setText(vehicleDto != null ? "Vehicle IP address: " + vehicleDto.getIpAddress() : "Vehicle not connected");
            mainForm.getLblVehicleIdVehicle2().setText(vehicleDto != null ? "Vehicle ID: " + vehicleDto.getId() : "Vehicle not connected");
            mainForm.getLblDiagnosticDataVehicle2().setText(diagnosticDataDto != null ? "Battery status: " + diagnosticDataDto.getBatteryChargeStatus()
                    + ", wheels turn measure: " + diagnosticDataDto.getWheelsTurnMeasure() : "No diagnostic data received");
            mainForm.getLblEncoderReadingVehicle2().setText(encoderReadingDto != null ? "Encoder reading: left front wheel: " + encoderReadingDto.getLeftFrontWheelSpeed()
                    + "..." : "No encoder readings received");
            mainForm.getLblImuReadingVehicle2().setText(imuReadingDto != null ? "IMU reading: acceleration X: " + imuReadingDto.getAccelerationX() + " ..." : "No IMU readings received");
            mainForm.getLblLidarReadingVehicle2().setText(lidarReadingDto != null ? "Lidar reading: " + lidarReadingDto.getLidarDistancesReading() : "No lidar readings received");
            mainForm.getLblLocationVehicle2().setText(locationDto != null ? "Location: real X: " + locationDto.getRealXCoordinate()
                    + ", real Y: " + locationDto.getRealYCoordinate() : "No location data received");
            mainForm.getLblPointCloudReadingVehicle2().setText(pointCloudDto != null ? "Point cloud reading: " + pointCloudDto.getPointCloudReading() : "No point cloud reading received");
        }
    }

}
