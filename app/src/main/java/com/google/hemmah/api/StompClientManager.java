//package com.google.hemmah.api;
//
//
//import android.view.View;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import ua.naiksoftware.stomp.Stomp;
//import ua.naiksoftware.stomp.StompClient;
//
//public class StompClientManager {
//    private StompClient mStompClient;
//    private final SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
//    private Disposable mRestPingDisposable;
//    private CompositeDisposable compositeDisposable;
//
//    public StompClientManager(String stompApi) {
//        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, stompApi);
//        mStompClient.connect();
//    }
//
//    public void sendFromStomp(View v) {
//
//        compositeDisposable.add(mStompClient.send("/topic/hello-msg-mapping", "Echo STOMP " + mTimeFormat.format(new Date()))
//                .compose(applySchedulers())
//                .subscribe(() -> {
//                    Log.d(TAG, "STOMP echo send successfully");
//                }, throwable -> {
//                    Log.e(TAG, "Error send STOMP echo", throwable);
//                    toast(throwable.getMessage());
//                }));
//    }
//
//
//}
