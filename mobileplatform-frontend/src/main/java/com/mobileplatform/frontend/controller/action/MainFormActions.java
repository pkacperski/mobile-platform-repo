package com.mobileplatform.frontend.controller.action;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mobileplatform.frontend.controller.action.creation.Actions;
import com.mobileplatform.frontend.controller.api.RestHandler;
import com.mobileplatform.frontend.dto.LocationDto;
import com.mobileplatform.frontend.dto.VehicleConnectionStatus;
import com.mobileplatform.frontend.dto.VehicleDto;
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

    private RestHandler<DrivingModeDataDto> drivingModeDataDtoRestHandler;
    private RestHandler<DrivingModeSteeringResponse> drivingModeSteeringResponseRestHandler;
    private RestHandler<EmergencyModeDataDto> emergencyModeDataDtoRestHandler;
    private RestHandler<EmergencyModeSteeringResponse> emergencyModeSteeringResponseRestHandler;
    private RestHandler<LocationDto[]> locationDtoListRestHandler;
    private RestHandler<VehicleDto> vehicleDtoRestHandler;
    private RestHandler<VehicleConnectResponse> vehicleConnectResponseRestHandler;

    final String VEHICLE_PATH = "/vehicle";
    final String DRIVING_MODE_DATA_PATH = "/driving-mode-data";
    final String EMERGENCY_MODE_DATA_PATH = "/emergency-mode-data";
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
    final int MAX_INIT_LIMIT = 99999;
    @Getter final int[] batteryLimitsAllData = {-MAX_INIT_LIMIT, MAX_INIT_LIMIT};
    @Getter final int[] wheelsTurnAngleLimitsAllData = {-MAX_INIT_LIMIT, MAX_INIT_LIMIT};
    @Getter final int[] camerasTurnAngleLimitsAllData = {-MAX_INIT_LIMIT, MAX_INIT_LIMIT};
    @Getter final int[] wheelsVelocityLimitsAllData = {-MAX_INIT_LIMIT, MAX_INIT_LIMIT};
    @Getter final int[] accelerometerLimitsAllData = {-MAX_INIT_LIMIT, MAX_INIT_LIMIT};
    @Getter final int[] gyroscopeLimitsAllData = {-MAX_INIT_LIMIT, MAX_INIT_LIMIT};
    @Getter final int[] magnetometerLimitsAllData = {-MAX_INIT_LIMIT, MAX_INIT_LIMIT};
    final Color COLOR_RED = new Color(220, 0, 0);
    final Color COLOR_YELLOW = new Color(255, 210, 0);
    final Color COLOR_BLUE = new Color(74, 136, 199);
    final Color COLOR_GREEN = new Color(131, 179, 92);

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

        drivingModeDataDtoRestHandler = new RestHandler<>(DrivingModeDataDto.class);
        drivingModeSteeringResponseRestHandler = new RestHandler<>(DrivingModeSteeringResponse.class);
        emergencyModeDataDtoRestHandler = new RestHandler<>(EmergencyModeDataDto.class);
        emergencyModeSteeringResponseRestHandler = new RestHandler<>(EmergencyModeSteeringResponse.class);
        locationDtoListRestHandler = new RestHandler<>(LocationDto[].class);
        vehicleDtoRestHandler = new RestHandler<>(VehicleDto.class);
        vehicleConnectResponseRestHandler = new RestHandler<>(VehicleConnectResponse.class);

        mainForm.getBtnConnectVehicle().addActionListener(e -> sendConnectVehicleSignal(VEHICLE_1));
        mainForm.getBtnDisconnectVehicle().addActionListener(e -> sendDisconnectVehicleSignal(VEHICLE_1));
        mainForm.getBtnEmergencyStop().addActionListener(e -> sendEmergencySignal(EmergencyMode.STOP, VEHICLE_1));
        mainForm.getBtnEmergencyAbort().addActionListener(e -> sendEmergencySignal(EmergencyMode.ABORT_MISSION_AND_RETURN, VEHICLE_1));
        mainForm.getBtnAutonomousDrivingMode().addActionListener(e -> sendDrivingModeSignal(DrivingMode.AUTONOMOUS, VEHICLE_1));
        mainForm.getBtnManualSteeringMode().addActionListener(e -> sendDrivingModeSignal(DrivingMode.MANUAL_STEERING, VEHICLE_1));
        mainForm.getBtnStream1().addActionListener(e -> sendChangeActiveStreamSignal(VEHICLE_1, STREAM_1));
        mainForm.getBtnStream2().addActionListener(e -> sendChangeActiveStreamSignal(VEHICLE_1, STREAM_2));
        mainForm.getBtnStream3().addActionListener(e -> sendChangeActiveStreamSignal(VEHICLE_1, STREAM_3));
        mainForm.getBtnShowLocationHistory().addActionListener(e -> createFrameWithLocationHistory(VEHICLE_1));
        mainForm.getBtnShowLidarOccupancyMap().addActionListener(e -> createFrameWithLidarOccupancyMap());
        mainForm.getBtnShowPointCloud().addActionListener(e -> createFrameWithPointCloud());
        mainForm.getBtnOpenVehicle2View().addActionListener(e -> createFrameForVehicle2());
        mainForm.getBtnOpenAllDataView().addActionListener(e -> createFrameWithAllDataView(VEHICLE_1));
        mainForm.getBtnSetLimitsBatteryAllData().addActionListener(e -> setLimitsBatteryAllData());
        mainForm.getBtnSetLimitsWheelsTurnAllData().addActionListener(e -> setLimitsWheelsTurnAllData());
        mainForm.getBtnSetLimitsCamerasTurnAllData().addActionListener(e -> setLimitsCamerasTurnAllData());
        mainForm.getBtnSetLimitsWheelsVelocityAllData().addActionListener(e -> setLimitsWheelsVelocityAllData());
        mainForm.getBtnSetLimitsAccelerometerAllData().addActionListener(e -> setLimitsAccelerometerAllData());
        mainForm.getBtnSetLimitsGyroscopeAllData().addActionListener(e -> setLimitsGyroscopeAllData());
        mainForm.getBtnSetLimitsMagnetometerAllData().addActionListener(e -> setLimitsMagnetometerAllData());

        // TODO - dodac docelowe akcje dla pojazdu 2

        mainForm.getProgressBarBatteryStatus().setValue(0);
        mainForm.getProgressBarWheelsTurnLeft().setValue(0);
        mainForm.getProgressBarWheelsTurnRight().setValue(0);
        mainForm.getProgressBarCamerasTurnLeft().setValue(0);
        mainForm.getProgressBarCamerasTurnRight().setValue(0);
    }

    private void sendConnectVehicleSignal(int whichVehicle) {

        String vehicleIp = mainForm.getTxtVehicleIp().getText(); // TODO - vehicle 2: String vehicleIp = (whichVehicle == 1) ? mainForm.getTxtVehicleIp().getText() : mainForm.getTxtVehicleIpVehicle2().getText();
        String vehicleName = mainForm.getTxtVehicleName().getText(); // TODO - vehicle 2: String vehicleName = (whichVehicle == 1) ? mainForm.getTxtVehicleName().getText() : mainForm.getTxtVehicleNameVehicle2().getText();
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
            mainForm.getLblCurrentModeAllData().setText("Not set");
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
            System.out.println("TODO - Vehicle 2"); // TODO - vehicle 2
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
            System.out.println("TODO - vehicle 2"); // TODO - vehicle 2
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
            if(emergencyMode.equals(EmergencyMode.STOP)) {
                mainForm.getLblCurrentMode().setText("Current: STOP");
                mainForm.getLblCurrentMode().setForeground(COLOR_RED);
                mainForm.getLblCurrentModeAllData().setText("STOP");
                mainForm.getLblCurrentModeAllData().setForeground(COLOR_RED);
            }
            else {
                mainForm.getLblCurrentMode().setText("Current: abort mission");
                mainForm.getLblCurrentMode().setForeground(COLOR_YELLOW);
                mainForm.getLblCurrentModeAllData().setText("abort mission");
                mainForm.getLblCurrentModeAllData().setForeground(COLOR_YELLOW);
            }
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
            if(drivingMode.equals(DrivingMode.AUTONOMOUS)) {
                mainForm.getLblCurrentMode().setText("Current: autonomous");
                mainForm.getLblCurrentMode().setForeground(COLOR_GREEN);
                mainForm.getLblCurrentModeAllData().setText("autonomous");
                mainForm.getLblCurrentModeAllData().setForeground(COLOR_GREEN);
            }
            else {
                mainForm.getLblCurrentMode().setText("Current: manual");
                mainForm.getLblCurrentMode().setForeground(COLOR_BLUE);
                mainForm.getLblCurrentModeAllData().setText("manual");
                mainForm.getLblCurrentModeAllData().setForeground(COLOR_BLUE);
            }
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
            frame.setSize(520, 520);
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

    private void createFrameWithAllDataView(int whichVehicle) { // TODO add functional components to panelVehicle2 - now only for testing & demonstration purposes
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("All data view - Vehicle " + whichVehicle);
            frame.setSize(1440, 810);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame.setContentPane((whichVehicle == 1) ? mainForm.getPanelAllDataVehicle1() : mainForm.getPanelAllDataVehicle2());
            frame.pack();
            frame.setVisible(true);
        });
    }

    private long getVehicleId(int whichVehicle) {
        return Long.parseLong((whichVehicle == 1)
                ? mainForm.getLblVehicleId().getText().substring(mainForm.getLblVehicleId().getText().indexOf(':') + 2)
                : /* mainForm.getLblVehicleIdVehicle2().getText().substring(mainForm.getLblVehicleIdVehicle2().getText().indexOf(':') + 2) */ "1"); // TODO - vehicle 2
    }

    private String getVehicleIp(int whichVehicle) {
        return (whichVehicle == 1)
                ? mainForm.getLblVehicleIp().getText().substring(mainForm.getLblVehicleIp().getText().indexOf(':') + 2)
                : /* mainForm.getLblVehicleIpVehicle2().getText().substring(mainForm.getLblVehicleIpVehicle2().getText().indexOf(':') + 2) */ "1"; // TODO - vehicle 2
    }

    private String getVehicleName(int whichVehicle) {
        return (whichVehicle == VEHICLE_1)
                ? mainForm.getLblVehicleName().getText().substring(mainForm.getLblVehicleName().getText().indexOf(':') + 2)
                : /* mainForm.getLblVehicleNameVehicle2().getText().substring(mainForm.getLblVehicleNameVehicle2().getText().indexOf(':') + 2) */ "1"; // TODO - vehicle 2
    }

    private void setLimitsBatteryAllData() {
        batteryLimitsAllData[0] = Integer.parseInt(mainForm.getTextFieldBatteryMinValue().getText());
        batteryLimitsAllData[1] = Integer.parseInt(mainForm.getTextFieldBatteryMaxValue().getText());
    }

    private void setLimitsWheelsTurnAllData() {
        wheelsTurnAngleLimitsAllData[0] = Integer.parseInt(mainForm.getTextFieldWheelsTurnAngleMinValue().getText());
        wheelsTurnAngleLimitsAllData[1] = Integer.parseInt(mainForm.getTextFieldWheelsTurnAngleMaxValue().getText());
    }

    private void setLimitsCamerasTurnAllData() {
        camerasTurnAngleLimitsAllData[0] = Integer.parseInt(mainForm.getTextFieldCamerasTurnAngleMinValue().getText());
        camerasTurnAngleLimitsAllData[1] = Integer.parseInt(mainForm.getTextFieldCamerasTurnAngleMaxValue().getText());
    }

    private void setLimitsWheelsVelocityAllData() {
        wheelsVelocityLimitsAllData[0] = Integer.parseInt(mainForm.getTextFieldWheelsVelocityMinValue().getText());
        wheelsVelocityLimitsAllData[1] = Integer.parseInt(mainForm.getTextFieldWheelsVelocityMaxValue().getText());
    }

    private void setLimitsAccelerometerAllData() {
        accelerometerLimitsAllData[0] = Integer.parseInt(mainForm.getTextFieldAccelerometerMinValue().getText());
        accelerometerLimitsAllData[1] = Integer.parseInt(mainForm.getTextFieldAccelerometerMaxValue().getText());
    }

    private void setLimitsGyroscopeAllData() {
        gyroscopeLimitsAllData[0] = Integer.parseInt(mainForm.getTextFieldGyroscopeMinValue().getText());
        gyroscopeLimitsAllData[1] = Integer.parseInt(mainForm.getTextFieldGyroscopeMaxValue().getText());
    }

    private void setLimitsMagnetometerAllData() {
        magnetometerLimitsAllData[0] = Integer.parseInt(mainForm.getTextFieldMagnetometerMinValue().getText());
        magnetometerLimitsAllData[1] = Integer.parseInt(mainForm.getTextFieldMagnetometerMaxValue().getText());
    }
}
