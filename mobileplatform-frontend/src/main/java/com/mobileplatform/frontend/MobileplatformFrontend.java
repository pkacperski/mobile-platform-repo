package com.mobileplatform.frontend;

import com.mobileplatform.frontend.controller.action.creation.ActionsFactory;
import com.mobileplatform.frontend.opencv.OpenCvHandler;
import com.mobileplatform.frontend.websockets.WebSocketFrontendClient;
import lombok.extern.java.Log;

import javax.swing.*;
import java.net.URISyntaxException;
import java.util.Objects;

@Log
public class MobileplatformFrontend {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Objects.requireNonNull(ActionsFactory.getActions("MainForm")).control();

            Thread webSocketClientThread = new Thread(WebSocketFrontendClient::initialize); // WS na telemetrie - localhost:8081
            webSocketClientThread.start();

            WebSocketFrontendClient firstStreamClient = new WebSocketFrontendClient("localhost", 8082, "first-stream");
            WebSocketFrontendClient secondStreamClient = new WebSocketFrontendClient("localhost", 8083, "second-stream");
            Thread firstStreamThread = new Thread(firstStreamClient);
            Thread secondStreamThread = new Thread(secondStreamClient);
            firstStreamThread.start();
            secondStreamThread.start();

            OpenCvHandler.initialize();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalStateException | UnsupportedLookAndFeelException | URISyntaxException e) {
            log.severe(e.getMessage());
        }
    }
}
