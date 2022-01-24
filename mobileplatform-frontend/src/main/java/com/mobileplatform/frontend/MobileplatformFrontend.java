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

    public final static boolean IS_TEST_ENV = true; // TODO - set to 'false' for working with vehicles in real environment
    public final static int VEHICLES_COUNT = 1; // same settings as in BE application - TODO - check: moze te same stale z BE i FE trzymac w jednym miejscu i z obu apek je importowac
    public final static int STREAMS_PER_VEHICLE_COUNT = 1; // TODO - set the appropriate streams per vehicle count
    public final static String TELEMETRY_API_SERVER_IP_TEST = "localhost";
    public final static String TELEMETRY_API_SERVER_IP_PROD = "10.0.0.201";
    public final static int TELEMETRY_API_PORT_NUMBER = 8080;
    public final static int TELEMETRY_SERVER_PORT_NUMBER = 8081; // by default, port 8080 used for serving backend services, 8081 - for telemetry, 8082 and above for video streams
    public final static int[] VIDEO_STREAMS_PORT_NUMBERS = {8082, 8083}; // possible to add more ports to handle more video streams
    public final static String TELEMETRY_SERVER_NAME = "telemetry";
    public static final String[] VIDEO_SERVER_NAMES = {"first-stream", "second-stream"}; // possible to add more ports to handle more video streams. the names are client-side only, changes don't matter

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Objects.requireNonNull(ActionsFactory.getActions("MainForm")).control();
//            MainFormActions.getInstance().getMainForm().getPanelLocationCoordinateSystem().add(new MainForm.GridPane()); // TODO - NPE -> trzeba dodac do JFrame i mozna tylko raz dodac content?

            Thread telemetryClientThread = new Thread(TelemetryClient::initialize);
            telemetryClientThread.start();

            VideoReceiveInit.initialize();
            VideoReceiveHandler.initialize(VEHICLES_COUNT, VIDEO_SERVER_NAMES);
        } catch (IllegalStateException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            log.severe(e.getMessage());
        }
    }
}
