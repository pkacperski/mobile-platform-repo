package com.mobileplatform.backend;

import com.mobileplatform.backend.opencv.VideoCaptureHandler;
import com.mobileplatform.backend.opencv.VideoCaptureImpl;
import com.mobileplatform.backend.websocket.TelemetryServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MobileplatformBackendApplication {

	public final static boolean IS_TEST_ENV = true; // TODO - set to 'false' when working with real vehicles
	public final static boolean IS_TEST_LIDAR_AND_PC_STREAMING = true; // TODO - only 'true' for testing purposes
	final static int VEHICLES_COUNT = 1; // TODO - set the appropriate vehicles count
	final static int STREAMS_PER_VEHICLE_COUNT = 1; // TODO - set the appropriate streams per vehicle count
	public final static String WEBSOCKET_SERVER_IP_ADDRESS = "localhost";
	public final static int TELEMETRY_SERVER_PORT_NUMBER = 8081; // by default, port 8080 used for serving backend services, 8081 - for telemetry, 8082 and above for video streams
	public final static int[] VIDEO_STREAMS_PORT_NUMBERS = {8082, 8083}; // possible to add more ports to handle more video streams when there are more than 2 vehicles
	public final static String SAVED_VIDEOS_DIRECTORY = System.getProperty("user.dir"); // folder destination for saving video files
	public final static int STREAMING_VIDEOS_HEIGHT = 480; // determines the quality of the video files which are being streamed to frontend application
	public final static int SAVED_VIDEOS_HEIGHT = 360; // determines the quality of the video files which are saved
	final static String[] REAL_STREAM_ADDRESSES = { // TODO - set the proper addresses of streams; TODO - brac adres streama z podanego IP na FE
			"rtmp://10.0.0.101:1935/live/rgb",
			"rtmp://10.0.0.101:1935/live/gs",
			"rtmp://10.0.0.102:1935/live/rgb",
			"rtmp://10.0.0.102:1935/live/gs"
	};
	final static String[] TEST_STREAMS_ADDRESSES = {
			"0",
			"rtmp://localhost/live/rgb",
			"rtmp://localhost/live/gs",
			"C:/Users/DELL/Documents/Mgr/rozne/video-theory.mp4",
			"C:/Users/DELL/Documents/Mgr/rozne/video-star.mp4",
			"C:/Users/DELL/Documents/Mgr/rozne/video-12angrymen.mp4",
			"C:/Users/DELL/Documents/Mgr/rozne/video-intouchables.avi",
			"C:/Users/DELL/Documents/Mgr/rozne/video-beauty.mp4",
			"C:/Users/DELL/Documents/Mgr/rozne/video-harry.mkv"
	};

	public static void main(String[] args) {
		SpringApplication.run(MobileplatformBackendApplication.class, args);

		Thread telemetryServerThread = new Thread(TelemetryServer::initialize);
		telemetryServerThread.start();

		VideoCaptureImpl.initialize();
		VideoCaptureHandler.initialize(VEHICLES_COUNT, STREAMS_PER_VEHICLE_COUNT, IS_TEST_ENV ? TEST_STREAMS_ADDRESSES : REAL_STREAM_ADDRESSES);
	}
}
