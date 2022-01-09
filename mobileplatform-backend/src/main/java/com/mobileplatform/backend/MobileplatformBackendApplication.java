package com.mobileplatform.backend;

import com.mobileplatform.backend.opencv.OpenCVTest;
import com.mobileplatform.backend.websocket.WebSocketBackendServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MobileplatformBackendApplication {
	public static void main(String[] args) {
//		SpringApplication.run(MobileplatformBackendApplication.class, args);
//		WebSocketBackendServer.initialize();
		OpenCVTest.testOpenCV();
	}
}
