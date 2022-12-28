//package com.google.hemmah.api;
//
//import static android.content.ContentValues.TAG;
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.WebSocket;
//import okhttp3.WebSocketListener;
//import okio.ByteString;
//import ua.naiksoftware.stomp.Stomp;
//import ua.naiksoftware.stomp.client.StompClient;
//
//public class WebSocketClient  {
//    private static final String websocketUrl = "http://192.168.1.7:8080/ws";
//    Listner mListner = new Listner();
//    StompClient stompClient;
//    public WebSocketClient() {
//        WebSocket client = new OkHttpClient()
//                .newWebSocket(new Request.Builder().url(websocketUrl).build(),mListner);
//
//        StompClient stompClient = Stomp.over(client);
//        stompClient.topic()
//
//    }
//
//    public void connectSocket() {
//        Log.d(TAG, "Connecting...");
//                Log.d(TAG, "Connecting in other thread.");
//                this.stompClient.connect();
//            }
//
//    public void disconnectSocket() {
//        this.stompClient.disconnect();
//    }
//
//    public void send(String path, String message) {
//        Log.d(TAG, "Sending message");
//        this.stompClient.send(path, message).subscribe();
//    }
//
//class Listner extends WebSocketListener{
//    public Listner() {
//        super();
//    }
//
//    @Override
//    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
//        super.onClosed(webSocket, code, reason);
//    }
//
//    @Override
//    public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
//        super.onClosing(webSocket, code, reason);
//    }
//
//    @Override
//    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
//        super.onFailure(webSocket, t, response);
//    }
//
//    @Override
//    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
//        super.onMessage(webSocket, text);
//    }
//
//    @Override
//    public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
//        super.onMessage(webSocket, bytes);
//    }
//
//    @Override
//    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
//        super.onOpen(webSocket, response);
//    }
//}
//
//
//}
