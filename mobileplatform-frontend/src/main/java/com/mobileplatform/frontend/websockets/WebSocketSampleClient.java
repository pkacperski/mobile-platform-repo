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
 * This example demonstrates how to create a websocket connection to a server. Only the most
 * important callbacks are overloaded.
 * https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ExampleClient.java
 */
public class WebSocketSampleClient extends WebSocketClient {

    private final Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create(); // to solve a problem with deserializing java.time.LocalDateTime by gson

    public WebSocketSampleClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // If you plan to refuse connection based on ip or http fields overload: onWebsocketHandshakeReceivedAsClient
        send("Hello, this is Client");
        System.out.println("opened connection");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received: " + message);

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
        WebSocketSampleClient webSocketSampleClient = new WebSocketSampleClient(new URI("ws://localhost:8081/chat"));
        webSocketSampleClient.connect();
    }

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
                    + vehicleDto.getVehicleName() : "No vehicle found");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}
