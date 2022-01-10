package com.mobileplatform.backend.opencv;

import com.mobileplatform.backend.websocket.WebSocketBackendServer;
import lombok.Data;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    public static void captureRgbStream() {

        System.out.println("OpenCV test");
        System.out.println("Loaded OpenCV: " + Core.getVersionString());

        final String myRtmpAddress = "rtmp://192.168.0.142:1935/live/cam"; // TODO check address before testing rtmp streaming
        final String shakira = "C:/Users/DELL/Documents/Mgr/rozne/video.mp4";
        final int webcam = 0; // webcam streaming only works when creating VideoCapture without apiPreference parameter (i.e. new VideoCapture(webcam))
        final VideoCapture videoCapture = new VideoCapture(webcam /*, Videoio.CAP_FFMPEG*/);

        System.out.println("Backend name: " + (videoCapture.isOpened() ? videoCapture.getBackendName() : "unknown, video stream not open"));
        System.out.println("Frames per second: " + (videoCapture.isOpened() ? videoCapture.get(Videoio.CAP_PROP_FPS) : "unknown, video stream not open"));

        // OpenCV's imshow() method for Java does not exist - handle displaying the image in Swing and awt
        Mat frame = new Mat();
        while(videoCapture.read(frame)) {
            if(frame.width() > 0 && frame.height() > 0) {
                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".bmp", frame, matOfByte);
                WebSocketBackendServer.getInstance().send(matOfByte.toArray());
            }
        }

        videoCapture.release();
    }

    public static void captureGrayscaleStream() {

        final String myRtmpAddressGrayscale = "rtmp://192.168.0.142:1935/live/gs"; // TODO check address before testing rtmp streaming
        final VideoCapture videoCaptureGrayScale = new VideoCapture(myRtmpAddressGrayscale, Videoio.CAP_FFMPEG);

        // OpenCV's imshow() method for Java does not exist - handle displaying the image in Swing and awt
        Mat frame = new Mat();
        while(videoCaptureGrayScale.read(frame)) { // TODO with a real stream instead check for(long i=0; i<100000000; i++) and inside check if(videoCapture.isOpened()) before videoCapture.read(frame);
            if(frame.width() > 0 && frame.height() > 0) {
                // TODO - send frame to FE using WebSockets - either OpenCV frame or a buffered image/icon after conversion
                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".bmp", frame, matOfByte);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byteArrayOutputStream.write(0x01);
                try {
                    byteArrayOutputStream.write(matOfByte.toArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                WebSocketBackendServer.getInstance().send(byteArrayOutputStream.toByteArray());
            }
        }

        videoCaptureGrayScale.release();
    }
}
