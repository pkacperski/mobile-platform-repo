package com.mobileplatform.frontend;

import com.mobileplatform.frontend.controller.action.creation.ActionsFactory;
import com.mobileplatform.frontend.opencv.VideoReceiveHandler;
import com.mobileplatform.frontend.opencv.VideoReceiveInit;
import com.mobileplatform.frontend.websockets.TelemetryClient;
import lombok.extern.java.Log;

import javax.swing.*;
import java.util.Objects;

@Log
public class MobileplatformFrontend {

    public final static int VEHICLES_COUNT = 1; // TODO - set the appropriate vehicles count (same setting as in BE application)
    public final static int STREAMS_PER_VEHICLE_COUNT = 2; // TODO - set the appropriate streams per vehicle count (same setting as in BE application)
    public final static boolean IS_TEST_ENV = false; // TODO - set to 'false' when working with vehicles in real environment
    public final static boolean IS_TEST_LIDAR_AND_PC_STREAMING = false; // only 'true' for testing & demonstration purposes during development
    public final static String TELEMETRY_API_SERVER_IP_TEST = "localhost"; // IP address of the telemetry server for testing purposes
    public final static String TELEMETRY_API_SERVER_IP_PROD = "10.0.0.201"; // IP address of the telemetry server in target configuration in local network (makes it possible to launch BE & FE on different computers, if needed)
    public final static int TELEMETRY_API_PORT_NUMBER = 8080; // port number used to send steering data to vehicle
    public final static int TELEMETRY_SERVER_PORT_NUMBER = 8081; // by default, port 8080 used for sending steering data, 8081 - for receiving telemetry data, 8082 and above for video streams
    public final static int[] VIDEO_STREAMS_PORT_NUMBERS = {8082, 8083}; // possible to add more ports to handle more video streams (from more vehicles, simultaneously)
    public final static int LIDAR_STREAM_PORT_NUMBER = 8086; // mocked stream port number for occupancy map from lidar, only for testing & demonstration purposes
    public final static int POINT_CLOUD_STREAM_PORT_NUMBER = 8087; // mocked stream port number for point cloud visualisation, only for testing & demonstration purposes
    public final static String TELEMETRY_SERVER_NAME = "telemetry"; // name of the backend server sending telemetry data
    public final static String[] VIDEO_SERVER_NAMES = {"first-stream", "second-stream", "third-stream"}; // possible to handle more video streams from each of the vehicles. these names are client-side only and changes don't matter
    public final static String MAIN_FORM_ACTIONS_TYPE = "MainForm"; // to get control of the GUI main form object

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Objects.requireNonNull(ActionsFactory.getActions(MAIN_FORM_ACTIONS_TYPE)).control();

            Thread telemetryClientThread = new Thread(TelemetryClient::initialize);
            telemetryClientThread.start();

            VideoReceiveInit.initialize();
            VideoReceiveHandler.initialize(VEHICLES_COUNT, VIDEO_SERVER_NAMES);
        } catch (IllegalStateException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            log.severe(e.getMessage());
        }
    }
}
