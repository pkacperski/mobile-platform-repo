package com.mobileplatform.frontend.form;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;

@Getter
public class MainForm {
    private JFrame frame;
    private JPanel panel;
    private JPanel panelVehicle1;
    private JLabel lblVehicleName;
    private JLabel lblAccelerometerReading;
    private JLabel lblGyroReading;
    private JLabel lblMagnetometerReading;
    private JLabel lblVideoStream;
    private JTabbedPane tabbedPane;
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
    private JButton btnStream1;
    private JButton btnStream2;
    private JButton btnStream3;
    private JProgressBar progressBarBatteryStatus;
    private JProgressBar progressBarWheelsTurnLeft;
    private JProgressBar progressBarCamerasTurnLeft;
    private JProgressBar progressBarCamerasTurnRight;
    private JProgressBar progressBarLeftFrontWheelSpeed;
    private JProgressBar progressBarLeftRearWheelSpeed;
    private JProgressBar progressBarRightFrontWheelSpeed;
    private JProgressBar progressBarRightRearWheelSpeed;
    private JButton btnShowLocationHistory;
    private JButton btnShowPointCloud;
    private JButton btnShowLidarOccupancyMap;
    private JButton btnOpenVehicle2View;
    private JPanel panelVehicle2;
    private JLabel lblCurrentMode;
    private JPanel panelAllDataVehicle1;
    private JPanel panelAllDataVehicle2;
    private JButton btnOpenAllDataView;
    private JLabel lblBatteryStatusAllData;
    private JTextField textFieldBatteryMinValue;
    private JLabel lblVehicleNameAllData;
    private JLabel lblVehicleIpAllData;
    private JLabel lblCurrentModeAllData;
    private JLabel lblWheelsTurnAngleAllData;
    private JLabel lblCamerasTurnAngleAllData;
    private JLabel lblWheelsVelocityAllData;
    private JLabel lblAccelerometerAllData;
    private JLabel lblGyroscopeAllData;
    private JLabel lblMagnetometerAllData;
    private JTextField textFieldWheelsTurnAngleMinValue;
    private JTextField textFieldCamerasTurnAngleMinValue;
    private JTextField textFieldWheelsVelocityMinValue;
    private JTextField textFieldAccelerometerMinValue;
    private JTextField textFieldGyroscopeMinValue;
    private JTextField textFieldMagnetometerMinValue;
    private JTextField textFieldBatteryMaxValue;
    private JTextField textFieldWheelsTurnAngleMaxValue;
    private JTextField textFieldCamerasTurnAngleMaxValue;
    private JTextField textFieldWheelsVelocityMaxValue;
    private JTextField textFieldAccelerometerMaxValue;
    private JTextField textFieldGyroscopeMaxValue;
    private JTextField textFieldMagnetometerMaxValue;
    private JButton btnSetLimitsBatteryAllData;
    private JButton btnSetLimitsWheelsTurnAllData;
    private JButton btnSetLimitsCamerasTurnAllData;
    private JButton btnSetLimitsWheelsVelocityAllData;
    private JButton btnSetLimitsAccelerometerAllData;
    private JButton btnSetLimitsGyroscopeAllData;
    private JButton btnSetLimitsMagnetometerAllData;
    private JLabel lblRealXCoordAllData;
    private JLabel lblRealYCoordAllData;
    private JLabel lblSlamXCoordAllData;
    private JLabel lblSlamYCoordAllData;
    private JLabel lblSlamRotationAllData;
    private JProgressBar progressBarWheelsTurnRight;

    public MainForm() {
        frame = new JFrame("MainForm");
        frame.setTitle("Steering Cockpit");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        progressBarWheelsTurnLeft.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        progressBarWheelsTurnLeft.setMaximum(90);
        progressBarCamerasTurnLeft.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setButtonsBorders();
    }

    private void setButtonsBorders() {
        final Color buttonBorderColor = new Color(128, 128, 128);
        final int thicknessFour = 4;
        final boolean roundedCornersFalse = false;
        final LineBorder buttonBorder = new LineBorder(buttonBorderColor, thicknessFour, roundedCornersFalse);

        for (JButton button : Arrays.asList(btnEmergencyStop, btnEmergencyAbort, btnAutonomousDrivingMode, btnManualSteeringMode, btnConnectVehicle, btnDisconnectVehicle,
                btnConnectVehicle, btnStream1, btnStream2, btnStream3, btnShowLocationHistory, btnShowPointCloud, btnShowLidarOccupancyMap, btnOpenVehicle2View, btnOpenAllDataView, btnSetLimitsBatteryAllData,
                btnSetLimitsWheelsTurnAllData, btnSetLimitsCamerasTurnAllData, btnSetLimitsWheelsVelocityAllData, btnSetLimitsAccelerometerAllData, btnSetLimitsGyroscopeAllData, btnSetLimitsMagnetometerAllData)) {
            button.setBorder(buttonBorder);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
