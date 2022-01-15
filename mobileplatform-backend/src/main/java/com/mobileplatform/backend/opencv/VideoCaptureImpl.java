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

import java.io.File;

import static com.mobileplatform.backend.MobileplatformBackendApplication.*;

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
    private final int vehicleNumber;
    private final int streamNumber;
    private boolean isStreamActive;
    private final String dateTimeNow;

    public VideoCaptureImpl(String streamAddress, VideoServer videoServer, int vehicleNumber, int streamNumber, boolean isStreamActive, String dateTimeNow) {
        this.streamAddress = streamAddress;
        this.videoServer = videoServer;
        this.vehicleNumber = vehicleNumber;
        this.streamNumber = streamNumber;
        this.isStreamActive = isStreamActive;
        this.dateTimeNow = dateTimeNow;
    }

    public static void initialize() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Loaded OpenCV: " + Core.getVersionString());
        // TODO - check: no need to load or import openh264-1.8.0-win64.dll file to save videos to .avi files with VideoWriter?
    }

    public void run() {
        final String BMP_FILE_EXTENSION = ".bmp";
        int fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G');
        VideoCapture videoCapture = this.streamAddress.equals("0") ? new VideoCapture(0) : new VideoCapture(this.streamAddress, Videoio.CAP_FFMPEG);
        double fps = videoCapture.get(Videoio.CAP_PROP_FPS);
        float originalVideoRatio = (float) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH) / (float) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        Size videoSaveSize = new Size(Math.round(originalVideoRatio * SAVED_VIDEOS_HEIGHT), SAVED_VIDEOS_HEIGHT);
        Mat frame = new Mat();

        String videoPath = SAVED_VIDEOS_DIRECTORY + File.separator + "saved-videos" + File.separator + dateTimeNow;
        File directory = new File(videoPath);
        if (!directory.exists()){
            try {
                if(!directory.mkdirs())
                    throw new Exception("Error - unable to create a new directory to save videos!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String videoFileName = videoPath + File.separator + "video-vehicle-" + vehicleNumber + "-stream-" + streamNumber + ".avi";
        VideoWriter videoWriter = new VideoWriter(videoFileName, fourcc, fps, videoSaveSize, true);

        while(videoCapture.read(frame)) {
            if(frame.width() > 0 && frame.height() > 0) {
                Mat resizedFrame = new Mat();
                Imgproc.resize(frame, resizedFrame, videoSaveSize);
                videoWriter.write(resizedFrame);
                if(isStreamActive) {
                    Mat resizedFrameFrontend = new Mat();
                    Size sizeFrontend = new Size(originalVideoRatio*360,360); // TODO - set correct size of images before sending to FE?
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
