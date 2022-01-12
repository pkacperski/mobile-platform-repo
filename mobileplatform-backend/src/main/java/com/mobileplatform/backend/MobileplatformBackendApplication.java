package com.mobileplatform.backend;

import com.mobileplatform.backend.opencv.OpenCvStreamCapture;
import com.mobileplatform.backend.websocket.WebSocketTelemetryServer;
import com.mobileplatform.backend.websocket.WebSocketVideoServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class MobileplatformBackendApplication {
	final static int VEHICLES_COUNT = 2;
	final static int STREAMS_PER_VEHICLE_COUNT = 3;
	public final static String WEBSOCKET_SERVER_IP_ADDRESS = "localhost";
	public static int FIRST_FREE_PORT_NUMBER = 8081; // 8080 used for serving backend services

	public static void main(String[] args) {
		SpringApplication.run(MobileplatformBackendApplication.class, args); // supposedly no need to create a new thread

		// TODO - proba: jeden WS server do wysylania calej telemetrii + dla kazdego streama oddzielny WS server
		// TODO - proba: 3 streamy per pojazd - przesylac websocketem tylko ten ktory jest aktywny, ale zrobic watki dla kazdego VideoCapture
		Thread webSocketTelemetryThread = new Thread(WebSocketTelemetryServer::initialize);
		webSocketTelemetryThread.start();

		ArrayList<WebSocketVideoServer> streamServers = new ArrayList<>();
		ArrayList<OpenCvStreamCapture> streamCaptures = new ArrayList<>();
		ArrayList<Thread> streamServerThreads = new ArrayList<>();
		ArrayList<Thread> streamCaptureThreads = new ArrayList<>();
		// TODO - docelowe adresy streamow:
		String[] streamAddresses = {"0", /*"C:/Users/DELL/Documents/Mgr/rozne/video-beauty-1.mp4",*/ "C:/Users/DELL/Documents/Mgr/rozne/video.mp4",
				"C:/Users/DELL/Documents/Mgr/rozne/video-12angrymen-1.mp4", "C:/Users/DELL/Documents/Mgr/rozne/video-12angrymen-1.mp4", "C:/Users/DELL/Documents/Mgr/rozne/video-theory-1.mp4",
				"C:/Users/DELL/Documents/Mgr/rozne/video-beauty-2.mp4", "C:/Users/DELL/Documents/Mgr/rozne/video-12angrymen-2.mp4", "C:/Users/DELL/Documents/Mgr/rozne/video-theory-2.mp4"};
		OpenCvStreamCapture.initialize();

		for (int i = 0; i < VEHICLES_COUNT*STREAMS_PER_VEHICLE_COUNT; i++) {
			streamServers.add(new WebSocketVideoServer(WEBSOCKET_SERVER_IP_ADDRESS, FIRST_FREE_PORT_NUMBER));
			FIRST_FREE_PORT_NUMBER += 1; // by default: 8080 used for serving backend services, 8081 used for handling telemetry sending via WebSockets, 8082 and greater used for streams handling
			streamServerThreads.add(new Thread(streamServers.get(i)));
			streamServerThreads.get(i).start();
			// TODO - streamindexy: jakos sprytniej okreslac na jaki ekran ma trafic dany stream?
			streamCaptures.add(new OpenCvStreamCapture(streamAddresses[i], i+1, streamServers.get(i)));
			streamCaptureThreads.add(new Thread(streamCaptures.get(i)));
			streamCaptureThreads.get(i).start();
		}
	}
}
