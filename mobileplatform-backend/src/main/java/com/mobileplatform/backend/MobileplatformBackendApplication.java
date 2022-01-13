package com.mobileplatform.backend;

import com.mobileplatform.backend.opencv.VideoCaptureImpl;
import com.mobileplatform.backend.opencv.VideoCaptureHandler;
import com.mobileplatform.backend.websocket.TelemetryServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MobileplatformBackendApplication {

	final static int VEHICLES_COUNT = 2;
	final static int STREAMS_PER_VEHICLE_COUNT = 3;
	public final static String WEBSOCKET_SERVER_IP_ADDRESS = "localhost";
	public static int FIRST_FREE_PORT_NUMBER = 8081; // 8080 used for serving backend services
	// TODO - docelowe adresy streamow:
	final static String[] STREAMS_ADDRESSES = {"0", "C:/Users/DELL/Documents/Mgr/rozne/video-star.mp4", "C:/Users/DELL/Documents/Mgr/rozne/video-12angrymen.mp4",
			"C:/Users/DELL/Documents/Mgr/rozne/video-intouchables.avi",	"C:/Users/DELL/Documents/Mgr/rozne/video-theory.mp4",
			"C:/Users/DELL/Documents/Mgr/rozne/video-harry.mkv", "C:/Users/DELL/Documents/Mgr/rozne/video-beauty.mp4"};

	public static void main(String[] args) {
		SpringApplication.run(MobileplatformBackendApplication.class, args); // supposedly no need to create a new thread

		Thread webSocketTelemetryThread = new Thread(TelemetryServer::initialize);
		webSocketTelemetryThread.start();

		VideoCaptureImpl.initialize();
		VideoCaptureHandler.initialize(VEHICLES_COUNT, STREAMS_PER_VEHICLE_COUNT, STREAMS_ADDRESSES);
	}
}
