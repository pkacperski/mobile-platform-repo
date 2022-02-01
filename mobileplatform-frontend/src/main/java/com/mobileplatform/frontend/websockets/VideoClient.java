package com.mobileplatform.frontend.websockets;

import com.mobileplatform.frontend.controller.action.MainFormActions;
import com.mobileplatform.frontend.opencv.VideoReceiveHandler;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_UNCHANGED;

/**
 * https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ExampleClient.java
 */
public class VideoClient extends WebSocketClient {

    private final int whichVehicle; // for video from which vehicle (tab) is this VideoClient responsible
    private final VideoStreamType streamType;

    public VideoClient(String ipAddress, int port, String serverName, int whichVehicle, VideoStreamType streamType) throws URISyntaxException {
        super(new URI("ws://" + ipAddress + ":" + port + "/" + serverName));
        this.whichVehicle = whichVehicle;
        this.streamType = streamType;
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        send("Hello, this is frontend");
        System.out.println("Opened connection");
    }

    @Override
    public void onMessage(ByteBuffer message) {

        MatOfByte matOfByte = new MatOfByte(message.array());
        Mat receivedFrame = Imgcodecs.imdecode(matOfByte, IMREAD_UNCHANGED);
        if(receivedFrame.width() > 0 && receivedFrame.height() > 0) {
            BufferedImage bufferedImage = mat2BufferedImage(receivedFrame);
            ImageIcon icon = new ImageIcon(bufferedImage);
            if(this.whichVehicle == 1 && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle().isEnabled()) { // first stream - only show stream when testing OR when the vehicle is connected
                if(this.streamType == VideoStreamType.CAMERA_STREAM)
                    MainFormActions.getInstance().getMainForm().getLblVideoStream().setIcon(icon);
                else if(this.streamType == VideoStreamType.LIDAR_STREAM)
                    VideoReceiveHandler.setLidarImageIcon(icon);
                else if(this.streamType == VideoStreamType.POINT_CLOUD_STREAM)
                    VideoReceiveHandler.setPcImageIcon(icon);
            }
            else if(this.whichVehicle == 2 /* && !MainFormActions.getInstance().getMainForm().getBtnConnectVehicle2().isEnabled()*/) { // second stream
                System.out.println("TODO - vehicle 2"); // TODO - vehicle 2
            }
        }
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace(); // If the error is fatal then onClose will be called additionally
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
