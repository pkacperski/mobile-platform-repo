package com.mobileplatform.backend.opencv;

import com.mobileplatform.backend.websocket.VideoServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.mobileplatform.backend.MobileplatformBackendApplication.*;

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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
        String dateTimeNow = dateTimeFormatter.format(LocalDateTime.now());
        ArrayList<Thread> videoServerThreads = new ArrayList<>();
        ArrayList<Thread> videoCaptureThreads = new ArrayList<>();

        for (int i = 0; i < vehiclesCnt; i++) {
            videoServers.add(new VideoServer(WEBSOCKET_SERVER_IP_ADDRESS, VIDEO_STREAMS_PORT_NUMBERS[i]));
            videoServerThreads.add(new Thread(videoServers.get(i)));
            videoServerThreads.get(i).start();
        }

        for (int i = 0; i < vehiclesCnt * streamsPerVehicleCnt; i++) {
            // whichVehicle variable determines on which FE screen the stream will be available. By default, the first streams for both vehicles are active
            videoCaptureImpls.add(new VideoCaptureImpl(streamsAddresses[i], videoServers.get(i/streamsPerVehicleCnt), (i/streamsPerVehicleCnt)+1,
                    (i%streamsPerVehicleCnt)+1, (i % streamsPerVehicleCnt == 0), dateTimeNow));
            videoCaptureThreads.add(new Thread(videoCaptureImpls.get(i)));
            videoCaptureThreads.get(i).start();
        }

        if(IS_TEST_ENV && IS_TEST_LIDAR_AND_PC_STREAMING) {
            videoServers.add(new VideoServer(WEBSOCKET_SERVER_IP_ADDRESS, 8084));
            videoServerThreads.add(new Thread(videoServers.get(videoServers.size()-1)));
            videoServerThreads.get(videoServerThreads.size()-1).start();
            videoCaptureImpls.add(new VideoCaptureImpl("rtmp://localhost/live/lidar", videoServers.get(videoServers.size()-1), 1,
                    0, true, dateTimeNow));
            videoCaptureThreads.add(new Thread(videoCaptureImpls.get(videoCaptureImpls.size()-1)));
            videoCaptureThreads.get(videoCaptureThreads.size()-1).start();

            videoServers.add(new VideoServer(WEBSOCKET_SERVER_IP_ADDRESS, 8085));
            videoServerThreads.add(new Thread(videoServers.get(videoServers.size()-1)));
            videoServerThreads.get(videoServerThreads.size()-1).start();
            videoCaptureImpls.add(new VideoCaptureImpl("rtmp://localhost/live/pc", videoServers.get(videoServers.size()-1), 1,
                    0, true, dateTimeNow));
            videoCaptureThreads.add(new Thread(videoCaptureImpls.get(videoCaptureImpls.size()-1)));
            videoCaptureThreads.get(videoCaptureThreads.size()-1).start();
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
