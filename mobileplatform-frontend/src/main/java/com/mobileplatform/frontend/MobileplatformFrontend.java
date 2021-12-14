package com.mobileplatform.frontend;

import com.mobileplatform.frontend.controller.action.MainFormActions;
import com.mobileplatform.frontend.controller.action.creation.ActionsFactory;
import com.mobileplatform.frontend.websockets.WebSocketSampleClient;
import lombok.extern.java.Log;

import javax.swing.*;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@Log
public class MobileplatformFrontend {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Objects.requireNonNull(ActionsFactory.getActions("MainForm")).control();
            // TODO - replace timer scheduling with WebSocket connection
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    MainFormActions.getInstance().refreshDataInMainPanel();
                }
            };
            long NO_DELAY = 0L;
            long PERIOD_1_SECOND = 1000L;
            timer.schedule(timerTask, NO_DELAY, PERIOD_1_SECOND);
            WebSocketSampleClient.initialize();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalStateException | UnsupportedLookAndFeelException | URISyntaxException e) {
            log.severe(e.getMessage());
        }
    }
}
