package com.mobileplatform.frontend.controller.common;

import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
@Log
public class SystemProperties {
    public Properties properties = new Properties();

    static {
        try(InputStream inputStream = SystemProperties.class.getClassLoader().getResourceAsStream("application.properties")) {
            if(inputStream == null) log.warning("Unable to find application.properties file");
            properties.load(inputStream);
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }
}
