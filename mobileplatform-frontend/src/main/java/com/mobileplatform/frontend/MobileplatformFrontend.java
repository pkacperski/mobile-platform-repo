package com.mobileplatform.frontend;

import com.mobileplatform.frontend.controller.action.creation.ActionsFactory;
import lombok.extern.java.Log;

import javax.swing.*;
import java.util.Objects;

@Log
public class MobileplatformFrontend {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Objects.requireNonNull(ActionsFactory.getActions("MainForm")).control();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            log.severe(e.getMessage());
        }
    }
}
