package com.mobileplatform.frontend.controller.action.creation;

import com.mobileplatform.frontend.controller.action.MainFormActions;

public class ActionsFactory {
    public static Actions getActions(String type) {
        if ("MainForm".equalsIgnoreCase(type)) return MainFormActions.getInstance();
        return null;
    }
}
