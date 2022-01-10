package com.mobileplatform.frontend.opencv;

import lombok.Data;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

@Data
public class OpenCvHandler {
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
    }

    public static BufferedImage mat2BufferedImage(Mat mat){
        // Source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
        // The output can be assigned either to a BufferedImage or to an Image

        int type = BufferedImage.TYPE_BYTE_GRAY;
        if(mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] b = new byte[bufferSize];
        mat.get(0, 0, b);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }
}
