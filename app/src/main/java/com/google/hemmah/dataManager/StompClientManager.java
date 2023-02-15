package com.google.hemmah.dataManager;

import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.hemmah.model.ModelJson;
import com.google.hemmah.model.MeetingRoom;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class StompClientManager {
    public static final String mVolunteerTempToken = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoidm9sdW50ZWVyMSIsImV4cCI6MTY3NjM1MzEyMywiaWF0IjoxNjc2MzE3MTIzLCJyb2xlcyI6IlVTRVIsVk9MVU5URUVSIn0.mmA01OtPZbSWciAikLTBQPU8oVkBNpe5JgfpUdrgOMtYSIro3_gaYFIpnMwldHjMdm3SwO2YdzJJzKVKNk_IKp1FeQQ4IlDNmAK7YgKeFtf41AVYfpu1OA94HxA_CKyrAeAT80pQzip5YhjPeyfpd-7HywVr1qYmiznRTDQUT1CxfnZDbwt4uVqVJbiTZmW4tI2ejZS07aSDhI4lt-uons2E2iT3yz9oZvFbdTQWwANp5xrv7-LacrLXaK5EtrBM4Ly7CJqjUfUIQSHrwXegpeNWF6ZEzDWxoSGMMBHFwyjCZhScYr0F5HCBT-umQKoeW7urBxidD-hoU1vwyP6o-Q";
    public static final String mDisabledTempToken = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiZGlzYWJsZWQxIiwiZXhwIjoxNjc2MzUyODQzLCJpYXQiOjE2NzYzMTY4NDMsInJvbGVzIjoiVVNFUixESVNBQkxFRCJ9.FssvHATCKAKr3lxq0g4J2s9ZlsYPVEomWj4fVVVRVWyeMqeXuRYkJ2CVJm1PKcGnjjvFnYx1McT9de_gCYJs9ooKbHkaFV-CwbkPV4Rr3rONvZKzSczR92vdje-3nqKnP9xMJam3c6jU2Kmv0eSn7WEOsPmtWrh63bA_VWl0bynKcRM4MHJ7nsX6kE2irjELbLbrlwiBDG8OheQ1xWYuYOndjCTyeTU9cJtxeCZNQ7sFWagY_4qnjHTrwtZgccLrVSqojjIk_MvWun-UwTBzEAtin8gVVhPCyCZeIfO634U1kaSC0JTu1gHYeIicYYYBKEdaga0A9k0GILBJiwq8HA";
    private static final String stompApi = "wss://api.hemmah.live/ws";
    public static final String TAG = "StompMessages";
    private Context mContext;
    Gson mGson = new GsonBuilder().create();
    private StompClient mStompClient;

    public StompClientManager(Context context) {
        this.mContext = context;
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, stompApi);
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d(TAG, "Stomp connection opened");
                            break;
                        case ERROR:
                            Log.d(TAG, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.d(TAG, "Stomp connection closed");
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.d(TAG, "Stomp failed server heartbeat");
                            break;
                    }
                });
    }


    public void subscribeOnTopic(String receiveTopic, String stompHeaderToken, Consumer<MeetingRoom> callback) {
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", stompHeaderToken));
        mStompClient.topic(receiveTopic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    MeetingRoom room = mGson.fromJson(topicMessage.getPayload(), MeetingRoom.class);
                    callback.accept(room);
                    Log.d(TAG, "Received " + room.toString());
                }, throwable -> {
                    Log.d(TAG, "Error on subscribe topic", throwable);
                });
        mStompClient.connect(headers);
    }
    public void sendToStomp(String topic, String message, String stompHeaderToken) {
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", stompHeaderToken));
        mStompClient.send(topic, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }



    private void convertJsonToString(ModelJson fromJson) {
        Log.d(TAG, fromJson.getMessage());
    }
    public void  discconectStomp(){
        mStompClient.disconnect();
    }

}
