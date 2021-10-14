package com.mobileplatform.frontend.form;

import lombok.Getter;

import javax.swing.*;

@Getter
public class MainForm {
    private JFrame frame;
    private JPanel panel;
    private JButton btnRestCall;
    private JTextField textFieldId;
    private JTextField textFieldBody;
    private JLabel lblCallContent;

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
