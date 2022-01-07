package com.mobileplatform.frontend;

import com.mobileplatform.frontend.controller.action.creation.ActionsFactory;
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
            WebSocketFrontendClient.initialize();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalStateException | UnsupportedLookAndFeelException | URISyntaxException e) {
            log.severe(e.getMessage());
        }
    }
}
