package com.mobileplatform.frontend.opencv;

import lombok.Data;
import org.opencv.core.Core;

@Data
public class VideoReceiveInit {

    public static void initialize() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Loaded OpenCV: " + Core.getVersionString());
    }
}
