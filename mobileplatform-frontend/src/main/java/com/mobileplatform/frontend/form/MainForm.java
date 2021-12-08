package com.mobileplatform.frontend.form;

import lombok.Getter;

import javax.swing.*;

@Getter
public class MainForm {
    private JFrame frame;
    private JPanel panel;
    private JButton btnRestCall;
    private JLabel lblVehicleName;
    private JLabel lblDiagnosticData;
    private JLabel lblEncoderReading;
    private JLabel lblImuReading;
    private JLabel lblLidarReading;
    private JLabel lblPointCloudReading;
    private JLabel lblLocation;
    private JLabel lblVideoStream;
    private JTabbedPane tabbedPane;
    private JPanel panelVehicle1;
    private JPanel panelAddNewVehicle;
    private JPanel panelVehicle2;

    public MainForm() {
        frame = new JFrame("MainForm");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
