package com.mobileplatform.frontend.opencv;

import com.mobileplatform.frontend.controller.action.MainFormActions;
import com.mobileplatform.frontend.websockets.VideoClient;
import com.mobileplatform.frontend.websockets.VideoStreamType;

import javax.swing.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.mobileplatform.frontend.MobileplatformFrontend.*;

public class VideoReceiveHandler {

    private static VideoReceiveHandler videoReceiveHandler;
    private static ArrayList<VideoClient> videoClients;

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
                setStreamButtonsActive(i);
                System.out.println("Video client thread");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        if(IS_TEST_ENV && IS_TEST_LIDAR_AND_PC_STREAMING) {
            // only for testing & demonstration purposes
            try {
                videoClients.add(new VideoClient(TELEMETRY_API_SERVER_IP_TEST, 8084, "lidar", 1, VideoStreamType.LIDAR_STREAM));
                videoClientThreads.add(new Thread(videoClients.get(videoClients.size()-1)));
                videoClientThreads.get(videoClientThreads.size()-1).start();
                videoClients.add(new VideoClient(TELEMETRY_API_SERVER_IP_TEST, 8085, "pc", 1, VideoStreamType.POINT_CLOUD_STREAM));
                videoClientThreads.add(new Thread(videoClients.get(videoClients.size()-1)));
                videoClientThreads.get(videoClientThreads.size()-1).start();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public static VideoReceiveHandler getInstance(int vehiclesCount, String[] streamsAddresses) {
        if(videoReceiveHandler == null)
            initialize(vehiclesCount, streamsAddresses);
        return videoReceiveHandler;
    }

    private static void setStreamButtonsActive(int whichVehicle) {
        if(whichVehicle == 0) {
            MainFormActions.getInstance().getMainForm().getBtnStream1().setEnabled(true);
            if(STREAMS_PER_VEHICLE_COUNT > 1)
                MainFormActions.getInstance().getMainForm().getBtnStream2().setEnabled(true);
            if(STREAMS_PER_VEHICLE_COUNT > 2)
                MainFormActions.getInstance().getMainForm().getBtnStream3().setEnabled(true);
        }
        else if(whichVehicle == 1) {
            MainFormActions.getInstance().getMainForm().getBtnStream1Vehicle2().setEnabled(true);
            if(STREAMS_PER_VEHICLE_COUNT > 1)
                MainFormActions.getInstance().getMainForm().getBtnStream2Vehicle2().setEnabled(true);
            if(STREAMS_PER_VEHICLE_COUNT > 2)
                MainFormActions.getInstance().getMainForm().getBtnStream3Vehicle2().setEnabled(true);
        }
    }

    // only for testing & demonstration purposes
    private static ImageIcon lidarImageIcon;
    private static ImageIcon pcImageIcon;

    public static ImageIcon getLidarImageIcon() {
        return lidarImageIcon;
    }

    public static ImageIcon getPcImageIcon() {
        return pcImageIcon;
    }

    public static void setLidarImageIcon(ImageIcon lidarImageIcon) {
        VideoReceiveHandler.lidarImageIcon = lidarImageIcon;
    }

    public static void setPcImageIcon(ImageIcon pcImageIcon) {
        VideoReceiveHandler.pcImageIcon = pcImageIcon;
    }

}
