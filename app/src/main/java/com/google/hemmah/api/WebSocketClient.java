//package com.google.hemmah.api;
//
//import static android.content.ContentValues.TAG;
//
//import okhttp3.WebSocket;
//import rx.schedulers.Schedulers;
//import ua.naiksoftware.stomp.Stomp;
//import ua.naiksoftware.stomp.client.StompClient;
//
//public class WebSocketClient  {
//    private static final String websocketApi = "http://192.168.1.7:8080/ws";
//
//    public WebSocketClient(String url) {
//        StompClient client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, websocketApi);
//        client.connect();
//
//    }
//
//    public void connectSocket() {
//        Log.d(TAG, "Connecting...");
//                Log.d(TAG, "Connecting in other thread.");
//                stompClient.connect();
//            }
//
//    public void disconnectSocket() {
//        stompClient.disconnect();
//    }
//
//    public void send(String path, String message) {
//        Log.d(TAG, "Sending message");
//        stompClient.send(path, message).subscribe();
//    }
//
//
//    private void handleConnectionLifecycle(LifecycleEvent event) {
//        switch (event.getType()) {
//            case OPENED:
//                Log.d(TAG,"################## ONLINE!");
//                break;
//            case ERROR:
//                Log.d(TAG, "################## ERROR! Trying to connect again...");
//                break;
//            case CLOSED:
//                Log.d(TAG, "################## OFFLINE!");
//                break;
//        }
//
//    }
//
//}
