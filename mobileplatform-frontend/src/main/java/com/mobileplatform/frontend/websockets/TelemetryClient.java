package com.mobileplatform.frontend.websockets;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mobileplatform.frontend.controller.action.MainFormActions;
import com.mobileplatform.frontend.dto.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static com.mobileplatform.frontend.MobileplatformFrontend.*;

/**
 * https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ExampleClient.java
 */
public class TelemetryClient extends WebSocketClient {

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
            telemetryClient = new TelemetryClient(new URI("ws://" + WEBSOCKET_SERVER_IP_ADDRESS + ":" + TELEMETRY_SERVER_PORT_NUMBER + "/" + TELEMETRY_SERVER_NAME));
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
        String firstTabVehicleIdText = MainFormActions.getInstance().getMainForm().getLblVehicleId().getText();
        Long firstTabVehicleId = !firstTabVehicleIdText.contains("not connected") ? Long.parseLong(firstTabVehicleIdText.substring(firstTabVehicleIdText.indexOf(':') + 2)) : -1;
        String secondTabVehicleIdText = MainFormActions.getInstance().getMainForm().getLblVehicleIdVehicle2().getText();
        Long secondTabVehicleId = !secondTabVehicleIdText.contains("not connected") ? Long.parseLong(secondTabVehicleIdText.substring(secondTabVehicleIdText.indexOf(':') + 2)) : -1;
        return (Objects.equals(receivedVehicleId, firstTabVehicleId) ? 1 : (Objects.equals(receivedVehicleId, secondTabVehicleId) ? 2 : -1));
    }

    private void handleMessageWithDiagnosticData(String message) {
        try {
            DiagnosticDataDto diagnosticDataDto = gson.fromJson(message, DiagnosticDataDto.class);
            if(diagnosticDataDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(diagnosticDataDto.getVehicleId());
                String diagnosticDataText = "Battery status: " + diagnosticDataDto.getBatteryChargeStatus() + ", wheels turn measure: " + diagnosticDataDto.getWheelsTurnMeasure();

                if(whichTabVehicle == 1 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle().isEnabled()) // only change the information displayed when the connection is active (TODO - do not receive incoming data for inactive vehicles)
                    MainFormActions.getInstance().getMainForm().getLblDiagnosticData().setText(diagnosticDataText);
                else if(whichTabVehicle == 2 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle2().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblDiagnosticDataVehicle2().setText(diagnosticDataText);
            }
        } catch (JsonSyntaxException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithEncoderReading(String message) {
        try {
            EncoderReadingDto encoderReadingDto = gson.fromJson(message, EncoderReadingDto.class);
            if(encoderReadingDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(encoderReadingDto.getVehicleId());
                String encoderReadingText = "Encoder reading: left front wheel: " + encoderReadingDto.getLeftFrontWheelSpeed() + "...";

                if(whichTabVehicle == 1 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblEncoderReading().setText(encoderReadingText);
                else if(whichTabVehicle == 2 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle2().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblEncoderReadingVehicle2().setText(encoderReadingText);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithImuReading(String message) {
        try {
            ImuReadingDto imuReadingDto = gson.fromJson(message, ImuReadingDto.class);
            if(imuReadingDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(imuReadingDto.getVehicleId());
                String imuReadingText = "IMU reading: acceleration X: " + imuReadingDto.getAccelerationX() + " ...";

                if(whichTabVehicle == 1 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblImuReading().setText(imuReadingText);
                else if(whichTabVehicle == 2 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle2().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblImuReadingVehicle2().setText(imuReadingText);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithLidarReading(String message) {
        try {
            LidarReadingDto lidarReadingDto = gson.fromJson(message, LidarReadingDto.class);
            if(lidarReadingDto != null) {
                int whichTabVehicle = findOnWhichTabIsVehicle(lidarReadingDto.getVehicleId());
                String lidarReadingText = "Lidar reading: " + lidarReadingDto.getLidarDistancesReading().substring(0, 15) + " ..."; // TODO - display full lidar reading

                if(whichTabVehicle == 1 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblLidarReading().setText(lidarReadingText);
                else if(whichTabVehicle == 2 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle2().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblLidarReadingVehicle2().setText(lidarReadingText);
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
                String locationText = "Location: real X: " + locationDto.getRealXCoordinate() + ", real Y: " + locationDto.getRealYCoordinate();

                if(whichTabVehicle == 1 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblLocation().setText(locationText);
                else if(whichTabVehicle == 2 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle2().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblLocationVehicle2().setText(locationText);
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
                String pointCloudText = "Point cloud reading: " + pointCloudDto.getPointCloudReading();

                if(whichTabVehicle == 1 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblPointCloudReading().setText(pointCloudText);
                else if(whichTabVehicle == 2 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle2().isEnabled())
                    MainFormActions.getInstance().getMainForm().getLblPointCloudReadingVehicle2().setText(pointCloudText);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithVehicleData(String message) {
        try {
            VehicleDto vehicleDto = gson.fromJson(message, VehicleDto.class);
            System.out.println(vehicleDto); // TODO - remove handling message with vehicle data if not necessary (vehicle connection status updated in MainFormActions)
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}
