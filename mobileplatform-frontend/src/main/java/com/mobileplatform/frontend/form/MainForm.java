package com.mobileplatform.frontend.form;

import lombok.Getter;

import javax.swing.*;

@Getter
public class MainForm {
    private JFrame frame;
    private JPanel panel;
    private JButton btnFetchData;
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
    private JPanel panelVehicle2;
    private JButton btnEmergencyStop;
    private JButton btnEmergencyAbort;
    private JButton btnAutonomousDrivingMode;
    private JButton btnManualSteeringMode;
    private JButton btnConnectVehicle;
    private JFormattedTextField txtVehicleIp;
    private JFormattedTextField txtVehicleName;
    private JLabel lblVehicleIp;
    private JLabel lblVehicleId;

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
