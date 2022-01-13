package com.mobileplatform.frontend.opencv;

import lombok.Data;
import org.opencv.core.Core;

@Data
public class VideoReceiveInit {
    /*
     * Test of OpenCV module dependencies added manually to IntelliJ instead of importing a Maven dependency.
     * Configuration steps (following https://www.youtube.com/watch?v=vLf3ZcFotyA):
     * 1) Download OpenCV 4.5.4 library from https://opencv.org/releases/ and extract the archive to a preferred location
     * 2) In your IDE, add opencv-454.jar file as a dependency and attach to it a .dll file from <your_opencv_installation_folder>/build/java folder:
     * 2.1) <your_opencv_installation_folder>/build/java/x64/opencv_454.dll if you are running on a 64-bit system
     * 2.2) <your_opencv_installation_folder>/build/java/x86/opencv_454.dll if you are running on a 32-bit system
     * You should then be able to run the test with a video file on your PC.
     * This configuration was tested with IntelliJ IDEA 2021.2.3 under Windows 10 64-bit.
     * */

    public static void initialize() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Loaded OpenCV: " + Core.getVersionString());
    }
}
