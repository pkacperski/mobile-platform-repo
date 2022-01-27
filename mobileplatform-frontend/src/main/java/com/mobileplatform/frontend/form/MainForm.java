package com.mobileplatform.frontend.form;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class MainForm {
    private JFrame frame;
    private JPanel panel;
    private JButton btnFetchData;
    private JLabel lblVehicleName;
    private JLabel lblAccelerometerReading;
    private JLabel lblGyroReading;
    private JLabel lblMagnetometerReading;
    private JLabel lblVideoStream;
    private JTabbedPane tabbedPane;
    private JPanel panelVehicle1;
    private JButton btnEmergencyStop;
    private JButton btnEmergencyAbort;
    private JButton btnAutonomousDrivingMode;
    private JButton btnManualSteeringMode;
    private JButton btnConnectVehicle;
    private JFormattedTextField txtVehicleIp;
    private JFormattedTextField txtVehicleName;
    private JLabel lblVehicleIp;
    private JLabel lblVehicleId;
    private JButton btnDisconnectVehicle;
    private JPanel panelVehicle2;
    private JLabel lblEncoderReadingVehicle2;
    private JButton btnFetchDataVehicle2;
    private JLabel lblVideoStreamVehicle2;
    private JLabel lblVehicleNameVehicle2;
    private JLabel lblVehicleIpVehicle2;
    private JLabel lblLocationVehicle2;
    private JLabel lblPointCloudReadingVehicle2;
    private JLabel lblLidarReadingVehicle2;
    private JLabel lblDiagnosticDataVehicle2;
    private JLabel lblVehicleIdVehicle2;
    private JButton btnConnectVehicle2;
    private JFormattedTextField txtVehicleNameVehicle2;
    private JFormattedTextField txtVehicleIpVehicle2;
    private JButton btnEmergencyStopVehicle2;
    private JButton btnEmergencyAbortVehicle2;
    private JButton btnAutonomousDrivingModeVehicle2;
    private JButton btnManualSteeringModeVehicle2;
    private JLabel lblImuReadingVehicle2;
    private JButton btnDisconnectVehicle2;
    private JButton btnStream2;
    private JButton btnStream1;
    private JButton btnStream3;
    private JButton btnStream1Vehicle2;
    private JButton btnStream2Vehicle2;
    private JButton btnStream3Vehicle2;
    private JProgressBar progressBarBatteryStatus;
    private JProgressBar progressBarWheelsTurnLeft;
    private JProgressBar progressBarWheelsTurnRight;
    private JProgressBar progressBarCamerasTurnLeft;
    private JProgressBar progressBarCamerasTurnRight;
    private JProgressBar progressBarLeftFrontWheelSpeed;
    private JProgressBar progressBarLeftRearWheelSpeed;
    private JProgressBar progressBarRightFrontWheelSpeed;
    private JProgressBar progressBarRightRearWheelSpeed;
    private JButton btnShowLocationHistory;
    private JButton btnShowPointCloud;
    private JButton btnShowLidarOccupancyMap;

    public MainForm() {
        frame = new JFrame("MainForm");
        frame.setTitle("Steering Cockpit");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        progressBarWheelsTurnLeft.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        progressBarCamerasTurnLeft.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
