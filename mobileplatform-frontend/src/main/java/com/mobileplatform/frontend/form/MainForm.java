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
    private JLabel lblCurrentMode;
    private JPanel panelAllDataVehicle1;
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
    private JPanel panelVehicle2;
    private JLabel lblVehicleNameVehicle2;
    private JLabel lblVehicleIpVehicle2;
    private JLabel lblVehicleIdVehicle2;
    private JProgressBar progressBarBatteryStatusVehicle2;
    private JProgressBar progressBarWheelsTurnRightVehicle2;
    private JProgressBar progressBarWheelsTurnLeftVehicle2;
    private JProgressBar progressBarCamerasTurnLeftVehicle2;
    private JProgressBar progressBarCamerasTurnRightVehicle2;
    private JButton btnShowLidarOccupancyMapVehicle2;
    private JButton btnShowPointCloudVehicle2;
    private JButton btnShowLocationHistoryVehicle2;
    private JProgressBar progressBarLeftFrontWheelSpeedVehicle2;
    private JProgressBar progressBarRightFrontWheelSpeedVehicle2;
    private JProgressBar progressBarLeftRearWheelSpeedVehicle2;
    private JProgressBar progressBarRightRearWheelSpeedVehicle2;
    private JLabel lblAccelerometerReadingVehicle2;
    private JLabel lblGyroReadingVehicle2;
    private JLabel lblMagnetometerReadingVehicle2;
    private JButton btnConnectVehicle2;
    private JButton btnDisconnectVehicle2;
    private JFormattedTextField txtVehicleNameVehicle2;
    private JFormattedTextField txtVehicleIpVehicle2;
    private JButton btnEmergencyStopVehicle2;
    private JButton btnEmergencyAbortVehicle2;
    private JButton btnAutonomousDrivingModeVehicle2;
    private JButton btnManualSteeringModeVehicle2;
    private JLabel lblCurrentModeVehicle2;
    private JButton btnOpenAllDataViewVehicle2;
    private JLabel lblVideoStreamVehicle2;
    private JButton btnStream1Vehicle2;
    private JButton btnStream2Vehicle2;
    private JButton btnStream3Vehicle2;
    private JPanel panelAllDataVehicle2;
    private JLabel lblVehicleNameAllDataVehicle2;
    private JLabel lblVehicleIpAllDataVehicle2;
    private JLabel lblCurrentModeAllDataVehicle2;
    private JLabel lblBatteryStatusAllDataVehicle2;
    private JLabel lblWheelsTurnAngleAllDataVehicle2;
    private JLabel lblCamerasTurnAngleAllDataVehicle2;
    private JLabel lblWheelsVelocityAllDataVehicle2;
    private JLabel lblAccelerometerAllDataVehicle2;
    private JLabel lblGyroscopeAllDataVehicle2;
    private JLabel lblMagnetometerAllDataVehicle2;
    private JTextField textFieldMagnetometerMinValueVehicle2;
    private JTextField textFieldGyroscopeMinValueVehicle2;
    private JTextField textFieldAccelerometerMinValueVehicle2;
    private JTextField textFieldWheelsVelocityMinValueVehicle2;
    private JTextField textFieldCamerasTurnAngleMinValueVehicle2;
    private JTextField textFieldWheelsTurnAngleMinValueVehicle2;
    private JTextField textFieldBatteryMinValueVehicle2;
    private JTextField textFieldBatteryMaxValueVehicle2;
    private JTextField textFieldWheelsTurnAngleMaxValueVehicle2;
    private JTextField textFieldCamerasTurnAngleMaxValueVehicle2;
    private JTextField textFieldWheelsVelocityMaxValueVehicle2;
    private JTextField textFieldAccelerometerMaxValueVehicle2;
    private JTextField textFieldGyroscopeMaxValueVehicle2;
    private JTextField textFieldMagnetometerMaxValueVehicle2;
    private JButton btnSetLimitsMagnetometerAllDataVehicle2;
    private JButton btnSetLimitsGyroscopeAllDataVehicle2;
    private JButton btnSetLimitsAccelerometerAllDataVehicle2;
    private JButton btnSetLimitsWheelsVelocityAllDataVehicle2;
    private JButton btnSetLimitsCamerasTurnAllDataVehicle2;
    private JButton btnSetLimitsWheelsTurnAllDataVehicle2;
    private JButton btnSetLimitsBatteryAllDataVehicle2;
    private JLabel lblRealXCoordAllDataVehicle2;
    private JLabel lblRealYCoordAllDataVehicle2;
    private JLabel lblSlamXCoordAllDataVehicle2;
    private JLabel lblSlamYCoordAllDataVehicle2;
    private JLabel lblSlamRotationAllDataVehicle2;

    public MainForm() {
        frame = new JFrame("MainForm");
        frame.setTitle("Steering Cockpit");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        progressBarWheelsTurnLeft.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        progressBarCamerasTurnLeft.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        progressBarWheelsTurnLeftVehicle2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        progressBarCamerasTurnLeftVehicle2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setButtonsBorders();
    }

    private void setButtonsBorders() {
        final Color buttonBorderColor = new Color(128, 128, 128);
        final int thicknessFour = 4;
        final boolean roundedCornersFalse = false;
        final LineBorder buttonBorder = new LineBorder(buttonBorderColor, thicknessFour, roundedCornersFalse);

        for (JButton button : Arrays.asList(btnEmergencyStop, btnEmergencyAbort, btnAutonomousDrivingMode, btnManualSteeringMode, btnConnectVehicle, btnDisconnectVehicle,
                btnConnectVehicle, btnStream1, btnStream2, btnStream3, btnShowLocationHistory, btnShowPointCloud, btnShowLidarOccupancyMap, btnOpenVehicle2View, btnOpenAllDataView,
                btnSetLimitsBatteryAllData, btnSetLimitsWheelsTurnAllData, btnSetLimitsCamerasTurnAllData, btnSetLimitsWheelsVelocityAllData, btnSetLimitsAccelerometerAllData,
                btnSetLimitsGyroscopeAllData, btnSetLimitsMagnetometerAllData, btnEmergencyStopVehicle2, btnEmergencyAbortVehicle2, btnAutonomousDrivingModeVehicle2, btnManualSteeringModeVehicle2,
                btnDisconnectVehicle2, btnConnectVehicle2, btnStream1Vehicle2, btnStream2Vehicle2, btnStream3Vehicle2, btnShowLocationHistoryVehicle2, btnShowPointCloudVehicle2,
                btnShowLidarOccupancyMapVehicle2, btnOpenAllDataViewVehicle2, btnSetLimitsBatteryAllDataVehicle2, btnSetLimitsWheelsTurnAllDataVehicle2, btnSetLimitsCamerasTurnAllDataVehicle2,
                btnSetLimitsWheelsVelocityAllDataVehicle2, btnSetLimitsAccelerometerAllDataVehicle2, btnSetLimitsGyroscopeAllDataVehicle2, btnSetLimitsMagnetometerAllDataVehicle2
        )) {
            button.setBorder(buttonBorder);
        }
    }
}
