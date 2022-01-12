package com.mobileplatform.backend.websocket;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobileplatform.backend.MobileplatformBackendApplication;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * https://github.com/TooTallNate/Java-WebSocket/wiki#server-example
 */
public class WebSocketTelemetryServer extends WebSocketServer {

    private static WebSocketTelemetryServer webSocketTelemetryServer; // singleton for sending telemetry data from appropriate services
    private static Gson gson; // for serializing telemetry data objects before sending them via WebSocket to the client
    private WebSocket connectedClient; // maintaining only one open connection per server

    private WebSocketTelemetryServer(InetSocketAddress address) {
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
        webSocketTelemetryServer = new WebSocketTelemetryServer(new InetSocketAddress(host, port));
        MobileplatformBackendApplication.FIRST_FREE_PORT_NUMBER += 1;
        webSocketTelemetryServer.run();
    }

    public static WebSocketTelemetryServer getInstance() {
        if(webSocketTelemetryServer == null) {
            initialize();
        }
        return webSocketTelemetryServer;
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
