package com.mobileplatform.frontend.opencv;

import com.mobileplatform.frontend.websockets.VideoClient;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.mobileplatform.frontend.MobileplatformFrontend.VIDEO_STREAMS_PORT_NUMBERS;
import static com.mobileplatform.frontend.MobileplatformFrontend.WEBSOCKET_SERVER_IP_ADDRESS;

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
                videoClients.add(new VideoClient(WEBSOCKET_SERVER_IP_ADDRESS, VIDEO_STREAMS_PORT_NUMBERS[i], videoServerNames[i], i+1));
                videoClientThreads.add(new Thread(videoClients.get(i)));
                videoClientThreads.get(i).start();

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
}
