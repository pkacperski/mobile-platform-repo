package com.mobileplatform.frontend.opencv;

import com.mobileplatform.frontend.controller.action.MainFormActions;
import com.mobileplatform.frontend.form.MainForm;
import com.mobileplatform.frontend.websockets.VideoClient;
import com.mobileplatform.frontend.websockets.VideoStreamType;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.mobileplatform.frontend.MobileplatformFrontend.*;

public class VideoReceiveHandler {

    private static VideoReceiveHandler videoReceiveHandler;
    private static ArrayList<VideoClient> videoClients;
    private static final MainForm mainForm = MainFormActions.getInstance().getMainForm();

    private VideoReceiveHandler(){
        videoClients = new ArrayList<>();
    }

    public static void initialize(int vehiclesCnt, String[] videoServerNames) {

        videoReceiveHandler = new VideoReceiveHandler();
        ArrayList<Thread> videoClientThreads = new ArrayList<>();

        for (int i = 0; i < vehiclesCnt; i++) {
            try {
                videoClients.add(new VideoClient(TELEMETRY_API_SERVER_IP_TEST, VIDEO_STREAMS_PORT_NUMBERS[i], videoServerNames[i], i+1, VideoStreamType.CAMERA_STREAM));
                videoClientThreads.add(new Thread(videoClients.get(i)));
                videoClientThreads.get(i).start();
                setStreamButtonsActive(i + 1);
                System.out.println("Video client thread");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private static VideoClient lidarMockVideoClient;
    private static VideoClient pointCloudMockVideoClient;
    private static Thread lidarMockVideoReceiveThread;
    private static Thread pointCloudMockVideoReceiveThread;

    public static void createMockLidarAndPointCloudStreamClients(int whichVehicle) {
        // create mocked lidar and point cloud stream clients - for testing and demonstration purposes only
        try {
            lidarMockVideoClient = new VideoClient(TELEMETRY_API_SERVER_IP_TEST, LIDAR_STREAM_PORT_NUMBER, "lidar", whichVehicle, VideoStreamType.LIDAR_STREAM);
            pointCloudMockVideoClient = new VideoClient(TELEMETRY_API_SERVER_IP_TEST, POINT_CLOUD_STREAM_PORT_NUMBER, "pc", whichVehicle, VideoStreamType.POINT_CLOUD_STREAM);
            lidarMockVideoReceiveThread = new Thread(lidarMockVideoClient);
            pointCloudMockVideoReceiveThread = new Thread(pointCloudMockVideoClient);
            lidarMockVideoReceiveThread.start();
            pointCloudMockVideoReceiveThread.start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void disableMockLidarAndPointCloudStreamClients() {
        lidarMockVideoClient = null;
        pointCloudMockVideoClient = null;
        lidarMockVideoReceiveThread.interrupt();
        pointCloudMockVideoReceiveThread.interrupt();
    }

    public static VideoReceiveHandler getInstance(int vehiclesCount, String[] streamsAddresses) {
        if(videoReceiveHandler == null)
            initialize(vehiclesCount, streamsAddresses);
        return videoReceiveHandler;
    }

    private static void setStreamButtonsActive(int whichVehicle) {
        if(whichVehicle == 1) {
            mainForm.getBtnStream1().setEnabled(true);
            if(STREAMS_PER_VEHICLE_COUNT > 1)
                mainForm.getBtnStream2().setEnabled(true);
            if(STREAMS_PER_VEHICLE_COUNT > 2)
                mainForm.getBtnStream3().setEnabled(true);
        }
        else if(whichVehicle == 2) {
            mainForm.getBtnStream1Vehicle2().setEnabled(true);
            if(STREAMS_PER_VEHICLE_COUNT > 1)
                mainForm.getBtnStream2Vehicle2().setEnabled(true);
            if(STREAMS_PER_VEHICLE_COUNT > 2)
                mainForm.getBtnStream3Vehicle2().setEnabled(true);
        }
    }

    // only for testing & demonstration purposes
    @Getter @Setter
    private static ImageIcon lidarImageIcon;
    @Getter @Setter
    private static ImageIcon pcImageIcon;
}
