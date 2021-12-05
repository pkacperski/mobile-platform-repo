package com.mobileplatform.frontend.controller.action;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mobileplatform.frontend.controller.action.creation.Actions;
import com.mobileplatform.frontend.controller.api.RestHandler;
import com.mobileplatform.frontend.dto.*;
import com.mobileplatform.frontend.form.MainForm;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Log
public class MainFormActions implements Actions {
    private static MainFormActions mainFormActions;

    private MainForm mainForm;
    private RestHandler<DiagnosticDataDto> diagnosticDataDtoRestHandler;
    private RestHandler<EncoderReadingDto> encoderReadingDtoRestHandler;
    private RestHandler<ImuReadingDto> imuReadingDtoRestHandler;
    private RestHandler<LidarReadingDto> lidarReadingDtoRestHandler;
    private RestHandler<LocationDto> locationDtoRestHandler;
    private RestHandler<PointCloudDto> pointCloudDtoRestHandler;
    private RestHandler<VehicleDto> vehicleDtoRestHandler;

    private MainFormActions() {}

    public static synchronized MainFormActions getInstance() {
        if(mainFormActions == null)
            mainFormActions = new MainFormActions();
        return mainFormActions;
    }

    @Override
    public void initialize() {
        mainForm = new MainForm();
        mainForm.getFrame().setVisible(true);

        diagnosticDataDtoRestHandler = new RestHandler<>(DiagnosticDataDto.class);
        encoderReadingDtoRestHandler = new RestHandler<>(EncoderReadingDto.class);
        imuReadingDtoRestHandler = new RestHandler<>(ImuReadingDto.class);
        lidarReadingDtoRestHandler = new RestHandler<>(LidarReadingDto.class);
        locationDtoRestHandler = new RestHandler<>(LocationDto.class);
        pointCloudDtoRestHandler = new RestHandler<>(PointCloudDto.class);
        vehicleDtoRestHandler = new RestHandler<>(VehicleDto.class);

        mainForm.getBtnRestCall().addActionListener(e -> onClickBtnRestCall());
    }

    @Override
    public void control() {
        initialize();
    }

    private void onClickBtnRestCall() {
        try {
            List<String> results = new ArrayList<>();
            results.add(vehicleDtoRestHandler.performGet("/vehicle", "1").toString());
            results.add(pointCloudDtoRestHandler.performGet("/point-cloud/newest", "1").toString()); // TODO - zapisac path-y w stalych
            results.add(locationDtoRestHandler.performGet("/location/newest", "1").toString());
            StringBuilder stringBuilder = new StringBuilder();
            Stream.of(results).forEach(e -> stringBuilder.append(e).append("\r\n"));
            mainForm.getLblCallContent().setText(stringBuilder.toString());
        } catch (UnirestException e) {
            log.severe(e.getMessage());
        }
    }
}
