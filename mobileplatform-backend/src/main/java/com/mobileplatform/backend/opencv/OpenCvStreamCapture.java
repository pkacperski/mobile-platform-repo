package com.mobileplatform.backend.opencv;

import com.mobileplatform.backend.websocket.WebSocketBackendServer;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OpenCvStreamCapture implements Runnable {

    private final String streamAddress;
    private final int streamIndex;

    public OpenCvStreamCapture(String streamAddress, int streamIndex) {
        this.streamAddress = streamAddress;
        this.streamIndex = streamIndex;
    }

    public void run() {
        VideoCapture videoCapture = new VideoCapture(streamAddress, Videoio.CAP_FFMPEG);
        Mat frame = new Mat();
        while(videoCapture.read(frame)) {
            if(frame.width() > 0 && frame.height() > 0) {

//                System.out.println("New frame!"); // TODO - usunac

                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".bmp", frame, matOfByte);

                int i = streamIndex;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                byteArrayOutputStream.write((byte) streamIndex); // TODO - pierwszy bajt mowi gdzie (na ktora karte) wysylac stream
                try {
                    byteArrayOutputStream.write(matOfByte.toArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                WebSocketBackendServer.getInstance().send(byteArrayOutputStream.toByteArray());
            }
        }

        videoCapture.release();
    }

//    public void run() { // OK
//        while(true) {
//            System.out.println("Thread");
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
