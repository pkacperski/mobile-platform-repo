package com.mobileplatform.frontend.websockets;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mobileplatform.frontend.controller.action.MainFormActions;
import com.mobileplatform.frontend.dto.*;
import com.mobileplatform.frontend.form.MainForm;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static com.mobileplatform.frontend.MobileplatformFrontend.*;

/**
 * https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ExampleClient.java
 */
public class TelemetryClient extends WebSocketClient {

    private final MainForm mainForm = MainFormActions.getInstance().getMainForm();
    private final MainFormActions mainFormActions = MainFormActions.getInstance();
    private final Color COLOR_RED = new Color(220, 0, 0);
    private final Color COLOR_GRAY = new Color(187, 187, 187);

    private static TelemetryClient telemetryClient;
    private static Gson gson; // To solve a problem with deserializing java.time.LocalDateTime by gson

    private TelemetryClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        send("Hello, this is frontend");
        System.out.println("Opened connection");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);

        if(isDiagnosticData(message))
            handleMessageWithDiagnosticData(message);
        else if(isEncoderReading(message))
            handleMessageWithEncoderReading(message);
        else if(isImuReading(message))
            handleMessageWithImuReading(message);
        else if(isLidarReading(message))
            handleMessageWithLidarReading(message);
        else if(isLocationData(message))
            handleMessageWithLocationData(message);
        else if(isPointCloudReading(message))
            handleMessageWithPointCloudReading(message);
        else if(isVehicleData(message))
            handleMessageWithVehicleData(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace(); // If the error is fatal then onClose will be called additionally
    }

    public static void initialize() {

        gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
        // TODO - obsluzyc niepowodzenie w tworzeniu klienta i polaczeniu WS
        try {
            telemetryClient = new TelemetryClient(new URI("ws://" + TELEMETRY_API_SERVER_IP_TEST + ":" + TELEMETRY_SERVER_PORT_NUMBER + "/" + TELEMETRY_SERVER_NAME));
            telemetryClient.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static TelemetryClient getInstance() {
        if(telemetryClient == null) {
            initialize();
        }
        return telemetryClient;
    }

    public static Gson getGson() {
        if(gson == null) {
            gson = Converters.registerLocalDateTime(new GsonBuilder()).create(); // To solve a problem with deserializing java.time.LocalDateTime by gson
        }
        return gson;
    }

    public void sendMessage(String message) {
        send(message);
    }

    // TODO zmienic to sprawdzanie - pola sa opcjonalne = dane moga przychodzic niekompletne i takie sprawdzanie nie przejdzie. rozw = obowiazkowa flaga z typem danej?
    private boolean isDiagnosticData(String message) {
        return message.contains("batteryChargeStatus");
    }

    private boolean isEncoderReading(String message) {
        return message.contains("WheelSpeed");
    }

    private boolean isImuReading(String message) {
        return message.contains("accelerationX");
    }

    private boolean isLidarReading(String message) {
        return message.contains("lidarDistancesReading");
    }

    private boolean isLocationData(String message) {
        return message.contains("XCoordinate");
    }

    private boolean isPointCloudReading(String message) {
        return message.contains("pointCloudReading");
    }

    private boolean isVehicleData(String message) {
        return message.contains("vehicleName");
    }

    private int findOnWhichTabIsVehicle(Long receivedVehicleId) {
        String firstTabVehicleIdText = mainForm.getLblVehicleId().getText();
        Long firstTabVehicleId = (!firstTabVehicleIdText.contains("not connected") && !firstTabVehicleIdText.equals("")) ? Long.parseLong(firstTabVehicleIdText.substring(firstTabVehicleIdText.indexOf(':') + 2)) : -1;
        String secondTabVehicleIdText = mainForm.getLblVehicleIdVehicle2().getText();
        Long secondTabVehicleId = (!secondTabVehicleIdText.contains("not connected") && !secondTabVehicleIdText.equals("")) ? Long.parseLong(secondTabVehicleIdText.substring(secondTabVehicleIdText.indexOf(':') + 2)) : -1;
        return (Objects.equals(receivedVehicleId, firstTabVehicleId) ? 1 : (Objects.equals(receivedVehicleId, secondTabVehicleId) ? 2 : -1));
    }

    private void handleMessageWithDiagnosticData(String message) {
        try {
            DiagnosticDataDto diagnosticDataDto = gson.fromJson(message, DiagnosticDataDto.class);
            if(diagnosticDataDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(diagnosticDataDto.getVehicleId());
                setDiagnosticDataValuesOnScreen(diagnosticDataDto, whichTabVehicle);
            }
        } catch (JsonSyntaxException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setDiagnosticDataValuesOnScreen(DiagnosticDataDto diagnosticDataDto, int whichTabVehicle) {

        if(IS_TEST_ENV || (whichTabVehicle == 1 && !mainForm.getBtnConnectVehicle().isEnabled())) { // only change the information displayed when the connection is active (TODO - do not receive incoming data for inactive vehicles)

            int batteryPercentage = (diagnosticDataDto.getBatteryChargeStatus() < 1.0)
                    ? (int)(diagnosticDataDto.getBatteryChargeStatus() * 100) : (diagnosticDataDto.getBatteryChargeStatus() <= 100)
                    ? diagnosticDataDto.getBatteryChargeStatus().intValue() : 0;
            mainForm.getProgressBarBatteryStatus().setValue(batteryPercentage);
            if(batteryPercentage < mainFormActions.getBatteryLimitsAllData()[0]) {
                mainForm.getLblBatteryStatusAllData().setText(batteryPercentage + "% ˅");
                mainForm.getLblBatteryStatusAllData().setForeground(COLOR_RED);
            } else if(batteryPercentage > mainFormActions.getBatteryLimitsAllData()[1]) {
                mainForm.getLblBatteryStatusAllData().setText(batteryPercentage + "% ˄");
                mainForm.getLblBatteryStatusAllData().setForeground(COLOR_RED);
            } else {
                mainForm.getLblBatteryStatusAllData().setText(batteryPercentage + "%");
                mainForm.getLblBatteryStatusAllData().setForeground(COLOR_GRAY);
            }

            mainForm.getProgressBarWheelsTurnLeft().setValue(0); // clear wheels' left turn indicator
            mainForm.getProgressBarWheelsTurnRight().setValue(0); // clear wheels' right turn indicator
            if(diagnosticDataDto.getWheelsTurnMeasure() > 0 && diagnosticDataDto.getWheelsTurnMeasure() < Math.PI/2) { // left turn
                int turnLeftValue = (int) (diagnosticDataDto.getWheelsTurnMeasure() * 180.0 / Math.PI) * 100 / 90; // convert radians to degrees and scale them to a (0; 90) range
                mainForm.getProgressBarWheelsTurnLeft().setValue(turnLeftValue);
                if(turnLeftValue < mainFormActions.getWheelsTurnAngleLimitsAllData()[0]) {
                    mainForm.getLblWheelsTurnAngleAllData().setText("Left turn by " + turnLeftValue + " deg ˅");
                    mainForm.getLblWheelsTurnAngleAllData().setForeground(COLOR_RED);
                } else if(turnLeftValue > mainFormActions.getWheelsTurnAngleLimitsAllData()[1]) {
                    mainForm.getLblWheelsTurnAngleAllData().setText("Left turn by " + turnLeftValue + " deg ˄");
                    mainForm.getLblWheelsTurnAngleAllData().setForeground(COLOR_RED);
                } else {
                    mainForm.getLblWheelsTurnAngleAllData().setText("Left turn by " + turnLeftValue + " deg");
                    mainForm.getLblWheelsTurnAngleAllData().setForeground(COLOR_GRAY);
                }
            }
            else if(diagnosticDataDto.getWheelsTurnMeasure() < 0 && diagnosticDataDto.getWheelsTurnMeasure() > -Math.PI/2) { // right turn
                int turnRightValue = (int) (diagnosticDataDto.getWheelsTurnMeasure() * 180.0 / Math.PI * -1) * 100 / 90; // convert radians to degrees and scale them to a (0; 90) range
                mainForm.getProgressBarWheelsTurnRight().setValue(turnRightValue);
                if(turnRightValue < mainFormActions.getWheelsTurnAngleLimitsAllData()[0]) {
                    mainForm.getLblWheelsTurnAngleAllData().setText("Right turn by " + turnRightValue + " deg ˅");
                    mainForm.getLblWheelsTurnAngleAllData().setForeground(COLOR_RED);
                } else if(turnRightValue > mainFormActions.getWheelsTurnAngleLimitsAllData()[1]) {
                    mainForm.getLblWheelsTurnAngleAllData().setText("Right turn by " + turnRightValue + " deg ˄");
                    mainForm.getLblWheelsTurnAngleAllData().setForeground(COLOR_RED);
                } else {
                    mainForm.getLblWheelsTurnAngleAllData().setText("Right turn by " + turnRightValue + " deg");
                    mainForm.getLblWheelsTurnAngleAllData().setForeground(COLOR_GRAY);
                }
            }

            mainForm.getProgressBarCamerasTurnLeft().setValue(0); // clear camera's left turn indicator
            mainForm.getProgressBarCamerasTurnRight().setValue(0); // clear camera's right turn indicator
            if(diagnosticDataDto.getCameraTurnAngle() > 0 && diagnosticDataDto.getCameraTurnAngle() < Math.PI/2) { // left turn
                int turnLeftValue = (int) (diagnosticDataDto.getCameraTurnAngle() * 180 / Math.PI) * 100 / 90; // convert radians to degrees and scale them to a (0; 90) range
                mainForm.getProgressBarCamerasTurnLeft().setValue(turnLeftValue);
                if(turnLeftValue < mainFormActions.getCamerasTurnAngleLimitsAllData()[0]) {
                    mainForm.getLblCamerasTurnAngleAllData().setText("Left turn by " + turnLeftValue + " deg ˅");
                    mainForm.getLblCamerasTurnAngleAllData().setForeground(COLOR_RED);
                } else if(turnLeftValue > mainFormActions.getCamerasTurnAngleLimitsAllData()[1]) {
                    mainForm.getLblCamerasTurnAngleAllData().setText("Left turn by " + turnLeftValue + " deg ˄");
                    mainForm.getLblCamerasTurnAngleAllData().setForeground(COLOR_RED);
                } else {
                    mainForm.getLblCamerasTurnAngleAllData().setText("Left turn by " + turnLeftValue + " deg");
                    mainForm.getLblCamerasTurnAngleAllData().setForeground(COLOR_GRAY);
                }
            }
            else if(diagnosticDataDto.getCameraTurnAngle() < 0 && diagnosticDataDto.getCameraTurnAngle() > -Math.PI/2) { // right turn
                int turnRightValue = (int) (diagnosticDataDto.getCameraTurnAngle() * 180 / Math.PI * -1) * 100 / 90; // convert radians to degrees and scale them to a (0; 90) range
                mainForm.getProgressBarCamerasTurnRight().setValue(turnRightValue);
                if(turnRightValue < mainFormActions.getCamerasTurnAngleLimitsAllData()[0]) {
                    mainForm.getLblCamerasTurnAngleAllData().setText("Right turn by " + turnRightValue + " deg ˅");
                    mainForm.getLblCamerasTurnAngleAllData().setForeground(COLOR_RED);
                } else if(turnRightValue > mainFormActions.getCamerasTurnAngleLimitsAllData()[1]) {
                    mainForm.getLblCamerasTurnAngleAllData().setText("Right turn by " + turnRightValue + " deg ˄");
                    mainForm.getLblCamerasTurnAngleAllData().setForeground(COLOR_RED);
                } else {
                    mainForm.getLblCamerasTurnAngleAllData().setText("Right turn by " + turnRightValue + " deg");
                    mainForm.getLblCamerasTurnAngleAllData().setForeground(COLOR_GRAY);
                }
            }
            // TODO - action when received battery status is out of ranges (0: 1) and (0; 100) and wheels' and camera's turn out of range (-PI/2; PI/2)
        }
        else if(whichTabVehicle == 2 && (IS_TEST_ENV || !mainForm.getBtnConnectVehicle2().isEnabled())) {
            // TODO - odbieranie diagnostic data dla drugiego pojazdu
            System.out.println("Vehicle 2 diagnostic data: " + diagnosticDataDto);
        }
    }

    private void handleMessageWithEncoderReading(String message) {
        try {
            EncoderReadingDto encoderReadingDto = gson.fromJson(message, EncoderReadingDto.class);
            if(encoderReadingDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(encoderReadingDto.getVehicleId());
                setEncoderReadingValuesOnScreen(encoderReadingDto, whichTabVehicle);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void setEncoderReadingValuesOnScreen(EncoderReadingDto encoderReadingDto, int whichTabVehicle) {

        if(IS_TEST_ENV || (whichTabVehicle == 1 && !mainForm.getBtnConnectVehicle().isEnabled())) {
            if(encoderReadingDto.getLeftFrontWheelSpeed() >= mainForm.getProgressBarLeftFrontWheelSpeed().getMinimum()
                    && encoderReadingDto.getLeftFrontWheelSpeed() <= mainForm.getProgressBarLeftFrontWheelSpeed().getMaximum())
                mainForm.getProgressBarLeftFrontWheelSpeed().setValue(encoderReadingDto.getLeftFrontWheelSpeed().intValue());
            if(encoderReadingDto.getRightFrontWheelSpeed() >= mainForm.getProgressBarRightFrontWheelSpeed().getMinimum()
                    && encoderReadingDto.getRightFrontWheelSpeed() <= mainForm.getProgressBarRightFrontWheelSpeed().getMaximum())
                mainForm.getProgressBarRightFrontWheelSpeed().setValue(encoderReadingDto.getRightFrontWheelSpeed().intValue());
            if(encoderReadingDto.getLeftRearWheelSpeed() >= mainForm.getProgressBarLeftRearWheelSpeed().getMinimum()
                    && encoderReadingDto.getLeftRearWheelSpeed() <= mainForm.getProgressBarLeftRearWheelSpeed().getMaximum())
                mainForm.getProgressBarLeftRearWheelSpeed().setValue(encoderReadingDto.getLeftRearWheelSpeed().intValue());
            if(encoderReadingDto.getRightRearWheelSpeed() >= mainForm.getProgressBarRightRearWheelSpeed().getMinimum()
                    && encoderReadingDto.getRightRearWheelSpeed() <= mainForm.getProgressBarRightRearWheelSpeed().getMaximum())
                mainForm.getProgressBarRightRearWheelSpeed().setValue(encoderReadingDto.getRightRearWheelSpeed().intValue());
            // TODO - action when wheels' speed out of range (0, 100)
            mainForm.getLblWheelsVelocityAllData().setText(
                    "<html>Left front: " + encoderReadingDto.getLeftFrontWheelSpeed().intValue() + " rpm<br>"
                    + "Right front: " + encoderReadingDto.getRightFrontWheelSpeed().intValue() + " rpm<br>"
                    + "Left rear: " + encoderReadingDto.getLeftRearWheelSpeed().intValue() + " rpm<br>"
                    + "Right rear: " + encoderReadingDto.getRightRearWheelSpeed().intValue() + " rpm"
            );
            if(encoderReadingDto.getLeftFrontWheelSpeed().intValue() < mainFormActions.getWheelsVelocityLimitsAllData()[0]
                    || encoderReadingDto.getRightFrontWheelSpeed().intValue() < mainFormActions.getWheelsVelocityLimitsAllData()[0]
                    || encoderReadingDto.getLeftRearWheelSpeed().intValue() < mainFormActions.getWheelsVelocityLimitsAllData()[0]
                    || encoderReadingDto.getRightRearWheelSpeed().intValue() < mainFormActions.getWheelsVelocityLimitsAllData()[0]
            ) {
                mainForm.getLblWheelsVelocityAllData().setText(mainForm.getLblWheelsVelocityAllData().getText() + " ˅<br></html>");
                mainForm.getLblWheelsVelocityAllData().setForeground(COLOR_RED);
            } else if(encoderReadingDto.getLeftFrontWheelSpeed().intValue() > mainFormActions.getWheelsVelocityLimitsAllData()[1]
                    || encoderReadingDto.getRightFrontWheelSpeed().intValue() > mainFormActions.getWheelsVelocityLimitsAllData()[1]
                    || encoderReadingDto.getLeftRearWheelSpeed().intValue() > mainFormActions.getWheelsVelocityLimitsAllData()[1]
                    || encoderReadingDto.getRightRearWheelSpeed().intValue() > mainFormActions.getWheelsVelocityLimitsAllData()[1]
            ) {
                mainForm.getLblWheelsVelocityAllData().setText(mainForm.getLblWheelsVelocityAllData().getText() + " ˄<br></html>");
                mainForm.getLblWheelsVelocityAllData().setForeground(COLOR_RED);
            } else {
                mainForm.getLblWheelsVelocityAllData().setText(mainForm.getLblWheelsVelocityAllData().getText() + "<br></html>");
                mainForm.getLblWheelsVelocityAllData().setForeground(COLOR_GRAY);
            }
        }
        else if(whichTabVehicle == 2 && (IS_TEST_ENV || !mainForm.getBtnConnectVehicle2().isEnabled())) {
            // TODO - odbieranie diagnostic data dla drugiego pojazdu
            System.out.println("Vehicle 2 encoder data: " + encoderReadingDto);
        }
    }

    private void handleMessageWithImuReading(String message) {
        try {
            ImuReadingDto imuReadingDto = gson.fromJson(message, ImuReadingDto.class);
            if(imuReadingDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(imuReadingDto.getVehicleId());
                setImuReadingValuesOnScreen(imuReadingDto, whichTabVehicle);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void setImuReadingValuesOnScreen(ImuReadingDto imuReadingDto, int whichTabVehicle) {
        // TODO - visualise IMU reading data
        String accelerationReadingText = "<html>Acceleration x: " + (imuReadingDto.getAccelerationX().toString().length() > 5
                    ? imuReadingDto.getAccelerationX().toString().substring(0, 6) : imuReadingDto.getAccelerationX()) + " m/s^2<br>"
                + "Acceleration y: " + (imuReadingDto.getAccelerationY().toString().length() > 5
                    ? imuReadingDto.getAccelerationY().toString().substring(0, 6) : imuReadingDto.getAccelerationY()) + " m/s^2<br>"
                + "Acceleration z: " + (imuReadingDto.getAccelerationZ().toString().length() > 5
                    ? imuReadingDto.getAccelerationZ().toString().substring(0, 6) : imuReadingDto.getAccelerationZ()) + " m/s^2";
        String gyroReadingText = "<html>Gyroscope x: " + (imuReadingDto.getAngularVelocityX().toString().length() > 5
                    ? imuReadingDto.getAngularVelocityX().toString().substring(0, 6) : imuReadingDto.getAngularVelocityX()) + " deg/s<br>"
                + "Gyroscope y: " + (imuReadingDto.getAngularVelocityY().toString().length() > 5
                    ? imuReadingDto.getAngularVelocityY().toString().substring(0, 6) : imuReadingDto.getAngularVelocityY()) + " deg/s<br>"
                + "Gyroscope z: " + (imuReadingDto.getAngularVelocityZ().toString().length() > 5
                    ? imuReadingDto.getAngularVelocityZ().toString().substring(0, 6) : imuReadingDto.getAngularVelocityZ()) + " deg/s";
        String magnetometerReadingText = "<html>Magnetometer x: " + (imuReadingDto.getMagneticFieldX().toString().length() > 5
                    ? imuReadingDto.getMagneticFieldX().toString().substring(0, 6) : imuReadingDto.getMagneticFieldX()) + " μT<br>"
                + "Magnetometer y: " + (imuReadingDto.getMagneticFieldY().toString().length() > 5
                    ? imuReadingDto.getMagneticFieldY().toString().substring(0, 6) : imuReadingDto.getMagneticFieldY()) + " μT<br>"
                + "Magnetometer z: " + (imuReadingDto.getMagneticFieldZ().toString().length() > 5
                    ? imuReadingDto.getMagneticFieldZ().toString().substring(0, 6) : imuReadingDto.getMagneticFieldZ()) + " μT";

        if(IS_TEST_ENV || (whichTabVehicle == 1 && !mainForm.getBtnConnectVehicle().isEnabled())) {
            mainForm.getLblAccelerometerReading().setText(accelerationReadingText);
            mainForm.getLblGyroReading().setText(gyroReadingText);
            mainForm.getLblMagnetometerReading().setText(magnetometerReadingText);

            if(imuReadingDto.getAccelerationX() < mainFormActions.getAccelerometerLimitsAllData()[0]
                    || imuReadingDto.getAccelerationY() < mainFormActions.getAccelerometerLimitsAllData()[0]
                    || imuReadingDto.getAccelerationZ() < mainFormActions.getAccelerometerLimitsAllData()[0]
            ) {
                mainForm.getLblAccelerometerAllData().setText(accelerationReadingText + " ˅<br></html>");
                mainForm.getLblAccelerometerAllData().setForeground(COLOR_RED);
            } else if(imuReadingDto.getAccelerationX() > mainFormActions.getAccelerometerLimitsAllData()[1]
                    || imuReadingDto.getAccelerationY() > mainFormActions.getAccelerometerLimitsAllData()[1]
                    || imuReadingDto.getAccelerationZ() > mainFormActions.getAccelerometerLimitsAllData()[1]) {
                mainForm.getLblAccelerometerAllData().setText(accelerationReadingText + " ˄<br></html>");
                mainForm.getLblAccelerometerAllData().setForeground(COLOR_RED);
            } else {
                mainForm.getLblAccelerometerAllData().setText(accelerationReadingText + "<br></html>");
                mainForm.getLblAccelerometerAllData().setForeground(COLOR_GRAY);
            }

            if(imuReadingDto.getAngularVelocityX() < mainFormActions.getGyroscopeLimitsAllData()[0]
                    || imuReadingDto.getAngularVelocityY() < mainFormActions.getGyroscopeLimitsAllData()[0]
                    || imuReadingDto.getAngularVelocityZ() < mainFormActions.getGyroscopeLimitsAllData()[0]
            ) {
                mainForm.getLblGyroscopeAllData().setText(gyroReadingText + " ˅<br></html>");
                mainForm.getLblGyroscopeAllData().setForeground(COLOR_RED);
            } else if(imuReadingDto.getAngularVelocityX() > mainFormActions.getGyroscopeLimitsAllData()[1]
                    || imuReadingDto.getAngularVelocityY() > mainFormActions.getGyroscopeLimitsAllData()[1]
                    || imuReadingDto.getAngularVelocityZ() > mainFormActions.getGyroscopeLimitsAllData()[1]) {
                mainForm.getLblGyroscopeAllData().setText(gyroReadingText + " ˄<br></html>");
                mainForm.getLblGyroscopeAllData().setForeground(COLOR_RED);
            } else {
                mainForm.getLblGyroscopeAllData().setText(gyroReadingText + "<br></html>");
                mainForm.getLblGyroscopeAllData().setForeground(COLOR_GRAY);
            }

            if(imuReadingDto.getMagneticFieldX() < mainFormActions.getMagnetometerLimitsAllData()[0]
                    || imuReadingDto.getMagneticFieldY() < mainFormActions.getMagnetometerLimitsAllData()[0]
                    || imuReadingDto.getMagneticFieldZ() < mainFormActions.getMagnetometerLimitsAllData()[0]
            ) {
                mainForm.getLblMagnetometerAllData().setText(magnetometerReadingText + " ˅<br></html>");
                mainForm.getLblMagnetometerAllData().setForeground(COLOR_RED);
            } else if(imuReadingDto.getMagneticFieldX() > mainFormActions.getMagnetometerLimitsAllData()[1]
                    || imuReadingDto.getMagneticFieldY() > mainFormActions.getMagnetometerLimitsAllData()[1]
                    || imuReadingDto.getMagneticFieldZ() > mainFormActions.getMagnetometerLimitsAllData()[1]) {
                mainForm.getLblMagnetometerAllData().setText(magnetometerReadingText + " ˄<br></html>");
                mainForm.getLblMagnetometerAllData().setForeground(COLOR_RED);
            } else {
                mainForm.getLblMagnetometerAllData().setText(magnetometerReadingText + "<br></html>");
                mainForm.getLblMagnetometerAllData().setForeground(COLOR_GRAY);
            }
        }
        else if(whichTabVehicle == 2 && (IS_TEST_ENV || !mainForm.getBtnConnectVehicle2().isEnabled()))
            // TODO - odbieranie imu data dla drugiego pojazdu
            mainForm.getLblImuReadingVehicle2().setText(accelerationReadingText);
    }

    private void handleMessageWithLidarReading(String message) {
        try {
            LidarReadingDto lidarReadingDto = gson.fromJson(message, LidarReadingDto.class);
            if(lidarReadingDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(lidarReadingDto.getVehicleId());
                String lidarReadingText = "Lidar reading: " + (lidarReadingDto.getLidarDistancesReading().length() > 10
                        ? lidarReadingDto.getLidarDistancesReading().substring(0, 10) : "No lidar readings received")
                        + " ..."; // TODO - display full lidar reading

                if(IS_TEST_ENV || (whichTabVehicle == 1 && !mainForm.getBtnConnectVehicle().isEnabled()))
                    System.out.println(lidarReadingText); // TODO - remove handling this message here if not necessary
                else if(whichTabVehicle == 2 && (IS_TEST_ENV || !mainForm.getBtnConnectVehicle2().isEnabled()))
                    mainForm.getLblLidarReadingVehicle2().setText(lidarReadingText);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithLocationData(String message) {
        try {
            LocationDto locationDto = gson.fromJson(message, LocationDto.class);
            if(locationDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(locationDto.getVehicleId());
                String locationText = "Location: real X: " + (locationDto.getRealXCoordinate().toString().length() > 5
                        ? locationDto.getRealXCoordinate().toString().substring(0, 6) : locationDto.getRealXCoordinate())
                            + ", real Y: " + (locationDto.getRealYCoordinate().toString().length() > 5
                        ? locationDto.getRealYCoordinate().toString().substring(0,6) : locationDto.getRealYCoordinate()
                );

                if(IS_TEST_ENV || (whichTabVehicle == 1 && !mainForm.getBtnConnectVehicle().isEnabled())) {
                    System.out.println(locationText); // TODO - zapisywanie danych w tablicy odczytow, zeby nie bylo koniecznosci strzelania do API BE po dane w MainFormActions
                    mainForm.getLblRealXCoordAllData().setText(locationDto.getRealXCoordinate().toString().length() > 5
                            ? locationDto.getRealXCoordinate().toString().substring(0, 6) : String.valueOf(locationDto.getRealXCoordinate()));
                    mainForm.getLblRealYCoordAllData().setText(locationDto.getRealYCoordinate().toString().length() > 5
                            ? locationDto.getRealYCoordinate().toString().substring(0,6) : String.valueOf(locationDto.getRealYCoordinate()));
                    mainForm.getLblSlamXCoordAllData().setText(locationDto.getSlamXCoordinate().toString().length() > 5
                            ? locationDto.getSlamXCoordinate().toString().substring(0,6) : String.valueOf(locationDto.getSlamXCoordinate()));
                    mainForm.getLblSlamYCoordAllData().setText(locationDto.getSlamYCoordinate().toString().length() > 5
                            ? locationDto.getSlamYCoordinate().toString().substring(0,6) : String.valueOf(locationDto.getSlamYCoordinate()));
                    mainForm.getLblSlamRotationAllData().setText(locationDto.getSlamRotation().toString().length() > 5
                            ? locationDto.getSlamRotation().toString().substring(0,6) : String.valueOf(locationDto.getSlamRotation()));
                }
                else if(whichTabVehicle == 2 && (IS_TEST_ENV || !mainForm.getBtnConnectVehicle2().isEnabled()))
                    mainForm.getLblLocationVehicle2().setText(locationText);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithPointCloudReading(String message) {
        try {
            PointCloudDto pointCloudDto = gson.fromJson(message, PointCloudDto.class);
            if(pointCloudDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(pointCloudDto.getVehicleId());
                String pointCloudText = "Point cloud reading: " + (pointCloudDto.getPointCloudReading().length() > 5
                        ? pointCloudDto.getPointCloudReading().substring(0, 6) : pointCloudDto.getPointCloudReading()
                );

                if(IS_TEST_ENV || (whichTabVehicle == 1 && !mainForm.getBtnConnectVehicle().isEnabled()))
                    System.out.println(pointCloudText); // TODO - target handling message with point cloud reading
                else if(whichTabVehicle == 2 && (IS_TEST_ENV || !mainForm.getBtnConnectVehicle2().isEnabled()))
                    mainForm.getLblPointCloudReadingVehicle2().setText(pointCloudText);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithVehicleData(String message) {
        try {
            VehicleDto vehicleDto = gson.fromJson(message, VehicleDto.class);
            System.out.println(vehicleDto); // TODO - remove handling message with vehicle data if not necessary (vehicle connection status updated in MainFormActions)
            // TODO - zapisac gdzies IP i nie wyswietlac go w GUI
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}
