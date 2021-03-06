package com.mobileplatform.backend.opencv;

import com.mobileplatform.backend.websocket.VideoServer;

import java.util.ArrayList;

import static com.mobileplatform.backend.MobileplatformBackendApplication.*;

public class VideoCaptureHandler {

    private static int vehiclesCount;
    private static int streamsPerVehicleCount;
    private static ArrayList<VideoServer> videoServers;
    private static ArrayList<Thread> videoServerThreads;
    private static VideoCaptureImpl[] videoCaptureImpls;
    private static Thread[] videoCaptureImplThreads;

    private VideoCaptureHandler(int vehiclesCnt, int streamsPerVehicleCnt){
        vehiclesCount = vehiclesCnt;
        streamsPerVehicleCount = streamsPerVehicleCnt;
        videoServers = new ArrayList<>();
        videoServerThreads = new ArrayList<>();
        videoCaptureImpls = new VideoCaptureImpl[vehiclesCount * streamsPerVehicleCount];
        videoCaptureImplThreads = new Thread[vehiclesCount * streamsPerVehicleCount];
    }

    public static void initialize(int vehiclesCnt, int streamsPerVehicleCnt) {

        new VideoCaptureHandler(vehiclesCnt, streamsPerVehicleCnt);

        for (int i = 0; i < vehiclesCnt; i++) {
            videoServers.add(new VideoServer(WEBSOCKET_SERVER_IP_ADDRESS, VIDEO_STREAMS_PORT_NUMBERS[i]));
            videoServerThreads.add(new Thread(videoServers.get(i)));
            videoServerThreads.get(i).start();
        }
    }

    public static void createVideoStreams(int whichVehicle, String vehicleAddress) {
        // only accepting ip addresses in form: "http://" + <ip address core> + ":" + <port number> + "/" + <suffix>
        String streamAddressCore = "rtmp://" + extractIpAddressCore(vehicleAddress) + ":1935/live"; // port 1935 is default for RTMP streams
        if(whichVehicle == 0)
            whichVehicle = 1; // to avoid ambiguity in indexing vehicle numbers (should be starting from 1 and not from 0)
        for (int i = 0; i < streamsPerVehicleCount; i++) {
            // whichVehicle variable determines on which FE screen the stream will be available. By default, the first streams for both vehicles are active
            videoCaptureImpls[(whichVehicle - 1) * streamsPerVehicleCount + i] = new VideoCaptureImpl(streamAddressCore + VIDEO_STREAMS_KEYS[i],
                    videoServers.get(whichVehicle - 1), whichVehicle, (whichVehicle - 1) * streamsPerVehicleCount + i, false);
            videoCaptureImplThreads[(whichVehicle - 1) * streamsPerVehicleCount + i] = new Thread(videoCaptureImpls[(whichVehicle - 1) * streamsPerVehicleCount + i]);
            videoCaptureImplThreads[(whichVehicle - 1) * streamsPerVehicleCount + i].start();
        }
        videoCaptureImpls[(whichVehicle - 1) * streamsPerVehicleCount].setStreamActive(true);
    }

    public static void disableVideoStreams(int whichVehicle) {
        if(whichVehicle == 0)
            whichVehicle = 1; // to avoid ambiguity in indexing vehicle numbers (should be starting from 1 and not from 0)
        for (int i = 0; i < streamsPerVehicleCount; i++) {
            videoCaptureImpls[(whichVehicle - 1) * streamsPerVehicleCount + i] = null;
            videoCaptureImplThreads[(whichVehicle - 1) * streamsPerVehicleCount + i].interrupt();
        }
    }

    private static VideoServer lidarMockVideoServer;
    private static VideoServer pointCloudMockVideoServer;
    private static VideoCaptureImpl lidarMockVideoCaptureImpl;
    private static VideoCaptureImpl pointCloudMockVideoCaptureImpl;
    private static Thread lidarMockVideoCaptureThread;
    private static Thread pointCloudMockVideoCaptureThread;

    public static void createMockLidarAndPointCloudStreams() {
        // create mocked lidar and point cloud streams for vehicle 1 (from given test source) - for testing and demonstration purposes only
        final int VEHICLE_ONE = 1;
        final int STREAM_NUMBER_SEVEN = 7;
        final int STREAM_NUMBER_EIGHT = 8;
        final boolean STREAM_ACTIVE = true;
        lidarMockVideoServer = new VideoServer(WEBSOCKET_SERVER_IP_ADDRESS, LIDAR_STREAM_PORT_NUMBER);
        pointCloudMockVideoServer = new VideoServer(WEBSOCKET_SERVER_IP_ADDRESS, POINT_CLOUD_STREAM_PORT_NUMBER);
        lidarMockVideoCaptureImpl = new VideoCaptureImpl(LIDAR_MOCK_STREAM_ADDRESS, lidarMockVideoServer, VEHICLE_ONE, STREAM_NUMBER_SEVEN, STREAM_ACTIVE);
        pointCloudMockVideoCaptureImpl = new VideoCaptureImpl(POINT_CLOUD_MOCK_STREAM_ADDRESS, pointCloudMockVideoServer, VEHICLE_ONE, STREAM_NUMBER_EIGHT, STREAM_ACTIVE);
        lidarMockVideoCaptureThread = new Thread(lidarMockVideoCaptureImpl);
        pointCloudMockVideoCaptureThread = new Thread(pointCloudMockVideoCaptureImpl);

        lidarMockVideoServer.start();
        pointCloudMockVideoServer.start();
        lidarMockVideoCaptureThread.start();
        pointCloudMockVideoCaptureThread.start();
    }

    public static void disableLidarAndPointCloudMockStreams() {
        lidarMockVideoCaptureThread.interrupt();
        pointCloudMockVideoCaptureThread.interrupt();
        try {
            lidarMockVideoServer.stop();
            pointCloudMockVideoServer.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lidarMockVideoCaptureImpl = null;
        pointCloudMockVideoCaptureImpl = null;
    }

    private static String extractIpAddressCore(String vehicleAddress) {
        String ipAddressCore = vehicleAddress.substring(vehicleAddress.indexOf("http://") + "http://".length());
        ipAddressCore = (ipAddressCore.indexOf(':') != -1) ? ipAddressCore.substring(0, ipAddressCore.indexOf(':')) : ipAddressCore;
        return ipAddressCore;
    }

    public static void handleChangingActiveStream(int whichVehicle, int whichStream) {
        if(whichVehicle == 0)
            whichVehicle = 1; // to avoid ambiguity in indexing vehicle numbers (should be starting from 1 and not from 0)
        // handling a message about which stream to activate - turn off all streams for the particular vehicle and then turn on the one from the message
        for(int i = 0; i < vehiclesCount; i++) {
            if(whichVehicle == i+1 && whichStream <= streamsPerVehicleCount) {
                for(int j = 0; j < streamsPerVehicleCount; j++) {
                    if(videoCaptureImpls[i*streamsPerVehicleCount + j] != null) {
                        videoCaptureImpls[i*streamsPerVehicleCount + j].setStreamActive(false);
                    }
                }
            }
            if(videoCaptureImpls[i*streamsPerVehicleCount + whichStream - 1] != null) {
                videoCaptureImpls[i*streamsPerVehicleCount + whichStream - 1].setStreamActive(true);
            }
        }
    }

}
