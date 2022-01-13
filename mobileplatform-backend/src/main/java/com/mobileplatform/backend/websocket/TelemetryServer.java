package com.mobileplatform.backend.websocket;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobileplatform.backend.MobileplatformBackendApplication;
import com.mobileplatform.backend.opencv.VideoCaptureHandler;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * https://github.com/TooTallNate/Java-WebSocket/wiki#server-example
 */
public class TelemetryServer extends WebSocketServer {

    private static TelemetryServer telemetryServer; // singleton for sending telemetry data from appropriate services
    private static Gson gson; // for serializing telemetry data objects before sending them via WebSocket to the client
    private WebSocket connectedClient; // maintaining only one open connection per server

    private TelemetryServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to WebSocket telemetry data server");
        conn.send( "New connection: " + handshake.getResourceDescriptor());
        System.out.println("New connection to " + conn.getRemoteSocketAddress());
        this.connectedClient = conn;
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        this.connectedClient = null;
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
        // receiving a message about which stream to activate - turn off all streams for the particular vehicle and then turn on the one from the message
        if(message.contains("stream") && message.contains("vehicle")) {
            int whichVehicle = Integer.parseInt(message.substring(message.indexOf(':') + 2, message.indexOf(':') + 3));
            int whichStream = Integer.parseInt(message.substring(message.indexOf('.') - 1, message.indexOf('.')));
            VideoCaptureHandler.handleChangingActiveStream(whichVehicle, whichStream);
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        System.out.println("Received ByteBuffer from "	+ conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("An error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("Backend WebSocket server started successfully");
    }

    public static void initialize() {
        final String host = MobileplatformBackendApplication.WEBSOCKET_SERVER_IP_ADDRESS;
        final int port = MobileplatformBackendApplication.FIRST_FREE_PORT_NUMBER;

        gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
        telemetryServer = new TelemetryServer(new InetSocketAddress(host, port));
        MobileplatformBackendApplication.FIRST_FREE_PORT_NUMBER += 1;
        telemetryServer.run();
    }

    public static TelemetryServer getInstance() {
        if(telemetryServer == null) {
            initialize();
        }
        return telemetryServer;
    }

    public static Gson getGson() {
        if(gson == null) {
            gson = Converters.registerLocalDateTime(new GsonBuilder()).create(); // To solve a problem with deserializing java.time.LocalDateTime by gson
        }
        return gson;
    }

    public void send(String message) {
        if(this.connectedClient != null) // TODO check -> wysylanie tylko do klienta z ktorym jest polaczenie, a nie broadcast do wsyzstkich
            this.connectedClient.send(message);
    }
}
