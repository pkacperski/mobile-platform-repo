package com.mobileplatform.backend;

import com.mobileplatform.backend.opencv.VideoCaptureHandler;
import com.mobileplatform.backend.opencv.VideoCaptureImpl;
import com.mobileplatform.backend.websocket.TelemetryServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MobileplatformBackendApplication {

	final static int VEHICLES_COUNT = 2; // TODO - set the appropriate vehicles count
	final static int STREAMS_PER_VEHICLE_COUNT = 2; // TODO - set the appropriate streams per vehicle count
	public final static boolean IS_SAVING_VIDEOS = true; // TODO - set to 'true' to save all video data coming from vehicles on hard disk; to 'false' not to save video data
	public final static boolean IS_TEST_LIDAR_AND_PC_STREAMING = false; // only 'true' for testing & demonstration purposes during development
	public final static String WEBSOCKET_SERVER_IP_ADDRESS = "localhost"; // WebSocket server address for exchanging data between backend and frontend applications
	public final static int TELEMETRY_SERVER_PORT_NUMBER = 8081; // by default, port 8080 used for serving backend API services, 8081 - for telemetry, 8082 and above for video streams
	public final static int[] VIDEO_STREAMS_PORT_NUMBERS = {8082, 8083}; // possible to add more ports to handle more video streams (from more vehicles, simultaneously)
	public final static String[] VIDEO_STREAMS_KEYS = {"/rgb", "/depth", "/third"}; // stream keys for building addresses of streams which should be opened after connecting with vehicle (have to be the same as those sent by the vehicle)
	public final static String SAVED_VIDEOS_DIRECTORY = System.getProperty("user.dir"); // folder destination for saving video files
	public final static int STREAMING_VIDEOS_HEIGHT = 480; // determines the quality of the video files which are being streamed to frontend application
	public final static int SAVED_VIDEOS_HEIGHT = 360; // determines the quality of the video files which are saved
	public final static String LIDAR_MOCK_STREAM_ADDRESS = "rtmp://localhost/live/lidar"; // mocked stream address for occupancy map from lidar, only for testing & demonstration purposes
	public final static String POINT_CLOUD_MOCK_STREAM_ADDRESS = "rtmp://localhost/live/pc"; // mocked stream address for point cloud visualisation, only for testing & demonstration purposes
	public final static int LIDAR_STREAM_PORT_NUMBER = 8086; // mocked stream port number for occupancy map from lidar, only for testing & demonstration purposes
	public final static int POINT_CLOUD_STREAM_PORT_NUMBER = 8087; // mocked stream port number for point cloud visualisation, only for testing & demonstration purposes

	public static void main(String[] args) {

		SpringApplication.run(MobileplatformBackendApplication.class, args);

		Thread telemetryServerThread = new Thread(TelemetryServer::initialize);
		telemetryServerThread.start();

		VideoCaptureImpl.initialize();
		VideoCaptureHandler.initialize(VEHICLES_COUNT, STREAMS_PER_VEHICLE_COUNT);
	}
}
