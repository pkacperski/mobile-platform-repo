package com.mobileplatform.frontend.opencv;

import com.mobileplatform.frontend.controller.action.MainFormActions;
import com.mobileplatform.frontend.websockets.VideoClient;

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
                videoClients.add(new VideoClient(TELEMETRY_API_SERVER_IP_TEST, VIDEO_STREAMS_PORT_NUMBERS[i], videoServerNames[i], i+1));
                videoClientThreads.add(new Thread(videoClients.get(i)));
                videoClientThreads.get(i).start();
                setStreamButtonsActive(i);
                System.out.println("Video client thread");
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

}
