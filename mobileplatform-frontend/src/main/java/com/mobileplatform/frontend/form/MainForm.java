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
    private JButton btnFetchData;
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
    private JButton btnOpenVehicle2View;
    private JPanel panelVehicle2;
    private JPanel panelVehicle2Deprecated;
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
    private JButton btnStream1Vehicle2;
    private JButton btnStream2Vehicle2;
    private JButton btnStream3Vehicle2;
    private JLabel lblCurrentEmergencyMode;
    private JLabel lblCurrentDrivingMode;
    private JPanel panelAllDataVehicle1;
    private JPanel panelAllDataVehicle2;
    private JButton btnOpenAllDataView;
    private JLabel lblBatteryStatusAllData;
    private JTextField textFieldBatteryMinimalValue;
    private JLabel lblVehicleNameAllData;
    private JLabel lblVehicleIpAllData;
    private JLabel lblEmergencyModeAllData;
    private JLabel lblDrivingModeAllData;
    private JLabel lblWheelsTurnAngleAllData;
    private JLabel lblCamerasTurnAngleAllData;
    private JLabel lblWheelsVelocityAllData;
    private JLabel lblAcceleometerAllData;
    private JLabel lblGyroscopeAllData;
    private JLabel lblMagnetometerAllData;
    private JTextField textFieldWheelsTurnAngleMinimalValue;
    private JTextField textFieldCamerasTurnAngleMinimalValue;
    private JTextField textFieldWheelsVelocityMinimalValue;
    private JTextField textFieldAccelerometerMinimalValue;
    private JTextField textFieldGyroscopeMinimalValue;
    private JTextField textFieldMagnetometerMinimalValue;
    private JTextField textFieldBatteryMaximalValue;
    private JTextField textFieldWheelsTurnAngleMaximalValue;
    private JTextField textFieldCamerasTurnAngleMaximalValue;
    private JTextField textFieldWheelsVelocityMaximalValue;
    private JTextField textFieldAccelerometerMaximalValue;
    private JTextField textFieldGyroscopeMaximalValue;
    private JTextField textFieldMagnetometerMaximalValue;
    private JButton btnSetRangeBatteryAllData;
    private JButton btnSetRangeWheelsTurnAllData;
    private JButton btnSetRangeCamerasTurnAllData;
    private JButton btnSetRangeWheelsVelocityAllData;
    private JButton btnSetRangeAccelerometerAllData;
    private JButton btnSetRangeGyroscopeAllData;
    private JButton btnSetRangeMagnetometerAllData;
    private JLabel lblRealXCoordAllData;
    private JLabel lblRealYCoordAllData;
    private JLabel lblSlamXCoordAllData;
    private JLabel lblSlamYCoordAllData;
    private JLabel lblSlamRotationAllData;

    public MainForm() {
        frame = new JFrame("MainForm");
        frame.setTitle("Steering Cockpit");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        progressBarWheelsTurnLeft.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        progressBarCamerasTurnLeft.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setButtonsBorders();
    }

    private void setButtonsBorders() {
        final Color buttonBorderColor = new Color(128, 128, 128);
        final int thicknessFour = 4;
        final boolean roundedCornersFalse = false;
        final LineBorder buttonBorder = new LineBorder(buttonBorderColor, thicknessFour, roundedCornersFalse);

        for (JButton button : Arrays.asList(btnFetchData, btnEmergencyStop, btnEmergencyAbort, btnAutonomousDrivingMode, btnManualSteeringMode, btnConnectVehicle, btnDisconnectVehicle,
                btnConnectVehicle, btnStream1, btnStream2, btnStream3, btnShowLocationHistory, btnShowPointCloud, btnShowLidarOccupancyMap, btnOpenVehicle2View, btnOpenAllDataView)) {
            button.setBorder(buttonBorder);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
