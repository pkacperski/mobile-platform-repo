package com.mobileplatform.backend;

import com.mobileplatform.backend.opencv.OpenCvStreamCapture;
import com.mobileplatform.backend.websocket.WebSocketBackendServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MobileplatformBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(MobileplatformBackendApplication.class, args); // supposedly no need to create a new thread

		// TODO - proba: jeden WS server (WebSocketBackendServer::initialize) do wysylania calej telemetrii + dla kazdego streama oddzielny WS server
		Thread webSocketTelemetryThread = new Thread(WebSocketBackendServer::initialize); // WS na telemetrie - localhost:8081
		webSocketTelemetryThread.start();

		WebSocketBackendServer firstStreamServer = new WebSocketBackendServer("localhost", 8082);
		WebSocketBackendServer secondStreamServer = new WebSocketBackendServer("localhost", 8083);
		// TODO - 2/3 streamy per pojazd - przesylac websocketem tylko ten ktory jest aktywny, ale zrobic watki dla kazdego VideoCapture
		Thread firstStreamServerThread = new Thread(firstStreamServer);
		Thread secondServerThread = new Thread(secondStreamServer);
		firstStreamServerThread.start();
		secondServerThread.start();

		OpenCvStreamCapture.initialize();
		OpenCvStreamCapture firstStreamCapture = new OpenCvStreamCapture("C:/Users/DELL/Documents/Mgr/rozne/video-dlugie.mp4", 1, firstStreamServer);
		OpenCvStreamCapture secondStreamCapture = new OpenCvStreamCapture("C:/Users/DELL/Documents/Mgr/rozne/video-dlugie.mp4", 2, secondStreamServer);
		Thread firstStreamCaptureThread = new Thread(firstStreamCapture);
		Thread secondStreamCaptureThread = new Thread(secondStreamCapture);
		firstStreamCaptureThread.start();
		secondStreamCaptureThread.start();
	}
}
