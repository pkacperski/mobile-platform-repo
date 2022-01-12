package com.mobileplatform.backend.opencv;

import com.mobileplatform.backend.websocket.WebSocketVideoServer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

public class OpenCvStreamCapture implements Runnable {

    private final String streamAddress;
    private final int streamIndex;
    private final WebSocketVideoServer webSocketVideoServer;

    public OpenCvStreamCapture(String streamAddress, int streamIndex, WebSocketVideoServer webSocketVideoServer) {
        this.streamAddress = streamAddress;
        this.streamIndex = streamIndex;
        this.webSocketVideoServer = webSocketVideoServer;
    }

    public static void initialize() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("OpenCV test");
        System.out.println("Loaded OpenCV: " + Core.getVersionString());
    }

    public void run() {
        final String BMP_FILE_EXTENSION = ".bmp";
        // TODO - remove webcam test:
        VideoCapture videoCapture = this.streamAddress.equals("0") ? new VideoCapture(0) : new VideoCapture(this.streamAddress, Videoio.CAP_FFMPEG);
        Mat frame = new Mat();

        while(videoCapture.read(frame)) {
            if(frame.width() > 0 && frame.height() > 0) {
                MatOfByte frameOfByte = new MatOfByte();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                Imgcodecs.imencode(BMP_FILE_EXTENSION, frame, frameOfByte);
                byteArrayOutputStream.write((byte) this.streamIndex); // TODO - gdzie na FE ma byc wyswietlony dany stream? teraz pierwszy bajt mowi gdzie (na ktora karte) wysylac stream - to juz chyba niepotrzebne skoro wysylanie zawsze tylko do odp. klienta WebSocketowego?
                try {
                    byteArrayOutputStream.write(frameOfByte.toArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.webSocketVideoServer.send(byteArrayOutputStream.toByteArray());
            }
        }
        videoCapture.release();
    }
}
