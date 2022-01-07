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

/**
 * https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ExampleClient.java
 */
public class WebSocketFrontendClient extends WebSocketClient {

    private static WebSocketFrontendClient webSocketFrontendClient;
    private static Gson gson; // To solve a problem with deserializing java.time.LocalDateTime by gson

    public WebSocketFrontendClient(URI serverURI) {
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

    public static void initialize() throws URISyntaxException {
        // More about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        final String host = "localhost"; // Host and port have to be the same as in backend server
        final int port = 8081;
        final String clientName = "frontend";

        gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
        webSocketFrontendClient = new WebSocketFrontendClient(new URI("ws://" + host + ":" + port + "/" + clientName));
        webSocketFrontendClient.connect();
    }

    public static WebSocketFrontendClient getInstance() throws URISyntaxException {
        if(webSocketFrontendClient == null) {
            initialize();
        }
        return webSocketFrontendClient;
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

    private void handleMessageWithDiagnosticData(String message) {
        try {
            DiagnosticDataDto diagnosticDataDto = gson.fromJson(message, DiagnosticDataDto.class);
            MainFormActions.getInstance().getMainForm().getLblDiagnosticData().setText(diagnosticDataDto != null ?
                    "Battery status: " + diagnosticDataDto.getBatteryChargeStatus() + ", wheels turn measure: " + diagnosticDataDto.getWheelsTurnMeasure()
                    : "No diagnostic data received");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithEncoderReading(String message) {
        try {
            EncoderReadingDto encoderReadingDto = gson.fromJson(message, EncoderReadingDto.class);
            MainFormActions.getInstance().getMainForm().getLblEncoderReading().setText(encoderReadingDto != null ?
                    "Encoder reading: left front wheel: " + encoderReadingDto.getLeftFrontWheelSpeed() + "..."
                    : "No encoder readings received");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithImuReading(String message) {
        try {
            ImuReadingDto imuReadingDto = gson.fromJson(message, ImuReadingDto.class);
            MainFormActions.getInstance().getMainForm().getLblImuReading().setText(imuReadingDto != null ? "IMU reading: acceleration X: "
                    + imuReadingDto.getAccelerationX() + " ..." : "No IMU readings received");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithLidarReading(String message) {
        try {
            LidarReadingDto lidarReadingDto = gson.fromJson(message, LidarReadingDto.class);
            MainFormActions.getInstance().getMainForm().getLblLidarReading().setText(lidarReadingDto != null ? "Lidar reading: "
                    + lidarReadingDto.getLidarDistancesReading() : "No lidar readings received");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithLocationData(String message) {
        try {
            LocationDto locationDto = gson.fromJson(message, LocationDto.class);
            MainFormActions.getInstance().getMainForm().getLblLocation().setText(locationDto != null ? "Location: real X: "
                    + locationDto.getRealXCoordinate() + ", real Y: " + locationDto.getRealYCoordinate() : "No location data received");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithPointCloudReading(String message) {
        try {
            PointCloudDto pointCloudDto = gson.fromJson(message, PointCloudDto.class);
            MainFormActions.getInstance().getMainForm().getLblPointCloudReading().setText(pointCloudDto != null ? "Point cloud reading: "
                    + pointCloudDto.getPointCloudReading() : "No point cloud reading received");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void handleMessageWithVehicleData(String message) {
        // TODO - handle adding new vehicle - send data to a new address(?), display data in a new tab
        try {
            VehicleDto vehicleDto = gson.fromJson(message, VehicleDto.class);
            MainFormActions.getInstance().getMainForm().getLblVehicleName().setText(vehicleDto != null ? "Vehicle name: "
                    + vehicleDto.getName() : "Vehicle not connected");
            MainFormActions.getInstance().getMainForm().getLblVehicleIp().setText(vehicleDto != null ? "Vehicle IP address: "
                    + vehicleDto.getIpAddress() : "Vehicle not connected");
            MainFormActions.getInstance().getMainForm().getLblVehicleId().setText(vehicleDto != null ? "Vehicle ID: " // TODO - oddzielnie ID z BD i z API sterujacego
                    + vehicleDto.getId() : "Vehicle not connected");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}