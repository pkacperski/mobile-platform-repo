package com.mobileplatform.frontend.websockets;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * This example demonstrates how to create a websocket connection to a server. Only the most
 * important callbacks are overloaded.
 * https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/ExampleClient.java
 */
public class WebSocketSampleClient extends WebSocketClient {

    public WebSocketSampleClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public WebSocketSampleClient(URI serverURI) {
        super(serverURI);
    }

    public WebSocketSampleClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Hello, it is me, Client");
        System.out.println("opened connection");
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received: " + message);
//        send("Received your message!");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println(
                "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                        + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }

    public static void initialize() throws URISyntaxException {
        WebSocketSampleClient webSocketSampleClient = new WebSocketSampleClient(new URI(
                "ws://localhost:8081/chat")); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        webSocketSampleClient.connect();
    }
}
