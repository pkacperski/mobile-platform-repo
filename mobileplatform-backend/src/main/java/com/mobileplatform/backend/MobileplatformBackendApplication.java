package com.mobileplatform.backend;

import com.mobileplatform.backend.opencv.OpenCvHandler;
import com.mobileplatform.backend.websocket.WebSocketBackendServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MobileplatformBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(MobileplatformBackendApplication.class, args); // supposedly no need to create a new thread
		Thread webSocketServerThread = new Thread(WebSocketBackendServer::initialize);
		webSocketServerThread.start();

		OpenCvHandler.initialize();
		Thread rgbStreamCaptureThread = new Thread(OpenCvHandler::captureRgbStream);
		Thread grayscaleStreamCaptureStream = new Thread(OpenCvHandler::captureGrayscaleStream);
		rgbStreamCaptureThread.start();
		grayscaleStreamCaptureStream.start();
	}
}
