package com.mobileplatform.backend.opencv;

import lombok.Data;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

@Data
public class OpenCVTest {
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

    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private static JFrame jFrame = new JFrame();
    private static JLabel jLabel = new JLabel();

    public static void testOpenCV() {

        System.out.println("OpenCV test");
        System.out.println("Loaded OpenCV: " + Core.getVersionString());

        final String myRtmpAddress = "rtmp://192.168.0.224:1935/live/rgb"; // TODO check address before testing rtmp streaming
        final String shakira = "C:/Users/DELL/Documents/Mgr/rozne/video.mp4"; // TODO specify a valid path to a video file on your PC
        final int webcam = 0;
        final VideoCapture videoCapture = new VideoCapture(shakira, Videoio.CAP_FFMPEG);
        System.out.println("Backend name: " + (videoCapture.isOpened() ? videoCapture.getBackendName() : "unknown, video stream not open"));
        System.out.println("Frames per second: " + (videoCapture.isOpened() ? videoCapture.get(Videoio.CAP_PROP_FPS) : "unknown, video stream not open"));

        jFrame.setLayout(new FlowLayout());
        jFrame.setSize(600, 450);
        jFrame.add(jLabel);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // OpenCV's imshow() method for Java does not exist - handle displaying the image in Swing and awt
        Mat frame = new Mat();
        while(videoCapture.read(frame)) { // TODO with a real stream instead check for(long i=0; i<100000000; i++) and inside check if(videoCapture.isOpened()) before videoCapture.read(frame);
            if(frame.width() > 0 && frame.height() > 0) {
                System.out.println("received");
                BufferedImage bufferedImage = mat2BufferedImage(frame);
                ImageIcon icon = new ImageIcon(bufferedImage);
                jLabel.setIcon(icon);
                try { // TODO delete this block (should be needed only for video files on your PC)
                    Thread.sleep(38);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        videoCapture.release();
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
