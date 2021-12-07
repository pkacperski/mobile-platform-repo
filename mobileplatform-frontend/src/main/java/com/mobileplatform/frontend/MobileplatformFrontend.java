package com.mobileplatform.frontend;

import com.mobileplatform.frontend.controller.action.MainFormActions;
import com.mobileplatform.frontend.controller.action.creation.ActionsFactory;
import lombok.extern.java.Log;

import javax.swing.*;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

@Log
public class MobileplatformFrontend {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Objects.requireNonNull(ActionsFactory.getActions("MainForm")).control();
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    MainFormActions.getInstance().refreshDataInMainPanel();
                }
            };
            long NO_DELAY = 0L;
            long PERIOD_1_SECOND = 1000L;
            timer.schedule(timerTask, NO_DELAY, PERIOD_1_SECOND);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalStateException | UnsupportedLookAndFeelException e) {
            log.severe(e.getMessage());
        }
    }
}
