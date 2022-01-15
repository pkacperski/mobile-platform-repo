package com.mobileplatform.backend.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class VideoServer extends WebSocketServer {

    private WebSocket connectedClient; // maintaining only one open connection per server

    public VideoServer(String ipAddress, int port) {
        super(new InetSocketAddress(ipAddress, port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to WebSocket video server");
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

    public void send(byte[] message) {
        if(this.connectedClient != null)
            this.connectedClient.send(message);
    }
}
