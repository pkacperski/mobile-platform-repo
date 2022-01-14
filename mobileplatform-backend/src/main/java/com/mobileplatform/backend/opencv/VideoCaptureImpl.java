package com.mobileplatform.backend.opencv;

import com.mobileplatform.backend.websocket.VideoServer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

public class VideoCaptureImpl implements Runnable {

    private final String streamAddress;
    private final VideoServer videoServer;
    private boolean isStreamActive;
    private static String dateTimeNow; // TODO - stworzyc folder z data w nazwie: https://stackoverflow.com/questions/28947250/create-a-directory-if-it-does-not-exist-and-then-create-the-files-in-that-direct

    public VideoCaptureImpl(String streamAddress, VideoServer videoServer, boolean isStreamActive) {
        this.streamAddress = streamAddress;
        this.videoServer = videoServer;
        this.isStreamActive = isStreamActive;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");
        dateTimeNow = dateTimeFormatter.format(LocalDateTime.now());
    }

    public static void initialize() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Loaded OpenCV: " + Core.getVersionString());
        // TODO - check: no need to load or import openh264-1.8.0-win64.dll file to save videos to .avi files with VideoWriter?
    }

    public void run() {
        final String BMP_FILE_EXTENSION = ".bmp";
        VideoCapture videoCapture = this.streamAddress.equals("0") ? new VideoCapture(0) : new VideoCapture(this.streamAddress, Videoio.CAP_FFMPEG);
        int fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G');
        double fps = videoCapture.get(Videoio.CAP_PROP_FPS);
        // Size originalSize = new Size(640, 360); // TODO - rozmiar video do zapisu
        Size originalSize = new Size((int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        Mat frame = new Mat();

        // TODO - nazwy plikow + stworzyc nowy folder z data w nazwie: https://stackoverflow.com/questions/28947250/create-a-directory-if-it-does-not-exist-and-then-create-the-files-in-that-direct
        VideoWriter videoWriter = new VideoWriter(
                "C:\\Users\\DELL\\Documents\\Mgr\\mobile-platform-repo\\mobile-platform-repo\\saved-videos\\" /*+ dateTimeNow */ + "video-test-" + ((this.streamAddress.length() > 10) ? this.streamAddress.substring(this.streamAddress.length() - 10, this.streamAddress.length() - 4) : "webcam") + ".avi",
                fourcc, fps, originalSize, true); // TODO - resizing image originalSize here?

        while(videoCapture.read(frame)) {
            if(frame.width() > 0 && frame.height() > 0) {
                // proba resize
                //Mat resizedFrameSaving = new Mat();
                //Imgproc.resize(frame, resizedFrameSaving, originalSize);
                // /proba resize
                videoWriter.write(frame);
                if(isStreamActive) {
                    Mat resizedFrameFrontend = new Mat();
                    Size sizeFrontend = new Size(400,300); // TODO - set correct size of images before sending to FE? + maintain original video proportions?
                    Imgproc.resize(frame, resizedFrameFrontend, sizeFrontend);
                    MatOfByte frameOfByte = new MatOfByte();
                    Imgcodecs.imencode(BMP_FILE_EXTENSION, resizedFrameFrontend, frameOfByte);
                    this.videoServer.send(frameOfByte.toArray());
                }
            }
        }
        videoCapture.release();
        videoWriter.release();
    }

    public void setStreamActive(boolean isStreamActive) {
        this.isStreamActive = isStreamActive;
    }
}
