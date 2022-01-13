package com.mobileplatform.backend.opencv;

import com.mobileplatform.backend.websocket.VideoServer;

import java.util.ArrayList;

import static com.mobileplatform.backend.MobileplatformBackendApplication.FIRST_FREE_PORT_NUMBER;
import static com.mobileplatform.backend.MobileplatformBackendApplication.WEBSOCKET_SERVER_IP_ADDRESS;

public class VideoCaptureHandler {

    private static VideoCaptureHandler videoCaptureHandler;
    private static ArrayList<VideoServer> videoServers;
    private static ArrayList<VideoCaptureImpl> videoCaptureImpls;
    private static int vehiclesCount;
    private static int streamsPerVehicleCount;

    private VideoCaptureHandler(){
        videoServers = new ArrayList<>();
        videoCaptureImpls = new ArrayList<>();
    }

    public static void initialize(int vehiclesCnt, int streamsPerVehicleCnt, String[] streamsAddresses) {

        videoCaptureHandler = new VideoCaptureHandler();
        vehiclesCount = vehiclesCnt;
        streamsPerVehicleCount = streamsPerVehicleCnt;
        ArrayList<Thread> videoServerThreads = new ArrayList<>();
        ArrayList<Thread> streamCaptureThreads = new ArrayList<>();

        for (int i = 0; i < vehiclesCnt; i++) {
            videoServers.add(new VideoServer(WEBSOCKET_SERVER_IP_ADDRESS, FIRST_FREE_PORT_NUMBER));
            FIRST_FREE_PORT_NUMBER += 1; // by default: 8080 used for serving backend services, 8081 used for handling telemetry sending via WebSockets, 8082 and greater used for streams handling
            videoServerThreads.add(new Thread(videoServers.get(i)));
            videoServerThreads.get(i).start();
        }

        for (int i = 0; i < vehiclesCnt * streamsPerVehicleCnt; i++) {
            // whichVehicle variable determines on which FE screen the stream will be available. By default, the first streams for both vehicles are active
            videoCaptureImpls.add(new VideoCaptureImpl(streamsAddresses[i], videoServers.get(i/streamsPerVehicleCnt), i/streamsPerVehicleCnt + 1, (i % streamsPerVehicleCnt == 0)));
            streamCaptureThreads.add(new Thread(videoCaptureImpls.get(i)));
            streamCaptureThreads.get(i).start();
        }
    }

    public static void handleChangingActiveStream(int whichVehicle, int whichStream) {
        // handling a message about which stream to activate - turn off all streams for the particular vehicle and then turn on the one from the message
        for(int i = 0; i < vehiclesCount; i++) {
            if(whichVehicle == i+1 && whichStream <= (i+1)*streamsPerVehicleCount) {
                for(int j = 0; j < streamsPerVehicleCount; j++) {
                    videoCaptureImpls.get(i*streamsPerVehicleCount + j).setStreamActive(false);
                }
            }
            videoCaptureImpls.get(whichStream - 1).setStreamActive(true);
        }
    }

    public static VideoCaptureHandler getInstance(int vehiclesCount, int streamsPerVehicleCount, String[] streamsAddresses) {
        if(videoCaptureHandler == null)
            initialize(vehiclesCount, streamsPerVehicleCount, streamsAddresses);
        return videoCaptureHandler;
    }
}
