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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mobileplatform.backend.MobileplatformBackendApplication.*;

public class VideoCaptureImpl implements Runnable {

    private final String streamAddress;
    private final VideoServer videoServer;
    private final int whichVehicle;
    private final int streamNumber;
    private boolean isStreamActive;
    private static String videoPath;

    public VideoCaptureImpl(String streamAddress, VideoServer videoServer, int whichVehicle, int streamNumber, boolean isStreamActive) {
        this.streamAddress = streamAddress;
        this.videoServer = videoServer;
        this.whichVehicle = whichVehicle;
        this.streamNumber = streamNumber;
        this.isStreamActive = isStreamActive;
    }

    public static void initialize() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Loaded OpenCV: " + Core.getVersionString());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");
        String dateTimeNow = dateTimeFormatter.format(LocalDateTime.now());
        videoPath = SAVED_VIDEOS_DIRECTORY + File.separator + "saved-videos" + File.separator + dateTimeNow;
        File directory = new File(videoPath);
        if(!directory.exists()){
            try {
                if(!directory.mkdirs())
                    throw new Exception("Error - unable to create a new directory to save videos!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        final String BMP_FILE_EXTENSION = ".bmp";
        int fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G');
        VideoCapture videoCapture = this.streamAddress.equals("0") ? new VideoCapture(0) : new VideoCapture(this.streamAddress, Videoio.CAP_FFMPEG);
        double fps = videoCapture.get(Videoio.CAP_PROP_FPS);
        float originalVideoRatio = (float) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH) / (float) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        Size videoSaveSize = new Size(Math.round(originalVideoRatio * SAVED_VIDEOS_HEIGHT), SAVED_VIDEOS_HEIGHT);
        Mat frame = new Mat();

        String videoFileName = videoPath + File.separator + "video-vehicle-" + whichVehicle + "-stream-" + (streamNumber + 1) + ".avi";
        VideoWriter videoWriter = new VideoWriter(videoFileName, fourcc, fps, videoSaveSize, true);

        while(!Thread.interrupted() && videoCapture.read(frame)) {
            if(frame.width() > 0 && frame.height() > 0) {
                if(IS_SAVING_VIDEOS) { // only save videos when the flag indicates so
                    Mat resizedFrame = new Mat();
                    Imgproc.resize(frame, resizedFrame, videoSaveSize);
                    videoWriter.write(resizedFrame);
                }
                if(isStreamActive) {
                    Mat resizedFrameFrontend = new Mat();
                    Size sizeFrontend = new Size(originalVideoRatio*STREAMING_VIDEOS_HEIGHT, STREAMING_VIDEOS_HEIGHT);
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
