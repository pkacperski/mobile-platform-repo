package com.mobileplatform.frontend.controller.action;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mobileplatform.frontend.controller.action.creation.Actions;
import com.mobileplatform.frontend.controller.api.RestHandler;
import com.mobileplatform.frontend.dto.TestDto;
import com.mobileplatform.frontend.form.MainForm;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.stream.Stream;

@Log
public class MainFormActions implements Actions {
    private static MainFormActions mainFormActions;

    private MainForm mainForm;
    private RestHandler<TestDto> restHandlerTestDtos;

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

        restHandlerTestDtos = new RestHandler<>(TestDto.class);

        mainForm.getBtnRestCall().addActionListener(e -> onClickBtnRestCall());
    }

    @Override
    public void control() {
        initialize();
    }

    private void onClickBtnRestCall() {
        try {
            TestDto tests = restHandlerTestDtos.performPost("/test",  mainForm.getTextFieldBody().getText(),"application/json");
            StringBuilder stringBuilder = new StringBuilder();
            Stream.of(tests).forEach(e -> stringBuilder.append(e));
            mainForm.getLblCallContent().setText(stringBuilder.toString());
        } catch (UnirestException e) {
            log.severe(e.getMessage());
        }
    }
}
