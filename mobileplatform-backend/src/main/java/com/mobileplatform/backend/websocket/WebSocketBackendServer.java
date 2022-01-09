package com.mobileplatform.backend.websocket;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mobileplatform.backend.model.domain.steering.DrivingModeData;
import com.mobileplatform.backend.model.domain.steering.EmergencyModeData;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * https://github.com/TooTallNate/Java-WebSocket/wiki#server-example
 */
public class WebSocketBackendServer extends WebSocketServer {

    private static WebSocketBackendServer webSocketBackendServer;
    private static Gson gson;

    public WebSocketBackendServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to backend WebSocket server");
        broadcast( "New connection: " + handshake.getResourceDescriptor());
        System.out.println("New connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
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
        final String host = "localhost";
        final int port = 8081;

        gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
        webSocketBackendServer = new WebSocketBackendServer(new InetSocketAddress(host, port));
        webSocketBackendServer.run();
    }

    public static WebSocketBackendServer getInstance() {
        if(webSocketBackendServer == null) {
            initialize();
        }
        return webSocketBackendServer;
    }

    public static Gson getGson() {
        if(gson == null) {
            gson = Converters.registerLocalDateTime(new GsonBuilder()).create(); // To solve a problem with deserializing java.time.LocalDateTime by gson
        }
        return gson;
    }

    public void send(String message) {
        broadcast(message); // TODO - only send the message to appropriate clients (e.g. only send the data from vehicle 2 to a client which presents data from this vehicle)
    }
}
