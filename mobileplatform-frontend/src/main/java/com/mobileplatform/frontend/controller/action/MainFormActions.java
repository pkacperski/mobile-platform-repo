package com.mobileplatform.frontend.controller.action;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mobileplatform.frontend.controller.action.creation.Actions;
import com.mobileplatform.frontend.controller.api.RestHandler;
import com.mobileplatform.frontend.dto.PointCloudDto;
import com.mobileplatform.frontend.form.MainForm;
import lombok.extern.java.Log;

import java.util.stream.Stream;

@Log
public class MainFormActions implements Actions {
    private static MainFormActions mainFormActions;

    private MainForm mainForm;
    private RestHandler<PointCloudDto> pointCloudDtoRestHandler; // TODO reszta

    private MainFormActions() {
    }

    public static synchronized MainFormActions getInstance() {
        if(mainFormActions == null)
            mainFormActions = new MainFormActions();
        return mainFormActions;
    }

    @Override
    public void initialize() {
        mainForm = new MainForm();
        mainForm.getFrame().setVisible(true);

        pointCloudDtoRestHandler = new RestHandler<>(PointCloudDto.class); // TODO reszta

        mainForm.getBtnRestCall().addActionListener(e -> onClickBtnRestCall());
    }

    @Override
    public void control() {
        initialize();
    }

    private void onClickBtnRestCall() {
        try {
            PointCloudDto result = pointCloudDtoRestHandler.performGet("/point-cloud/newest");
            StringBuilder stringBuilder = new StringBuilder();
            Stream.of(result).forEach(e -> stringBuilder.append(e));
            mainForm.getLblCallContent().setText(stringBuilder.toString());
        } catch (UnirestException e) {
            log.severe(e.getMessage());
        }
    }
}
