package com.google.hemmah.dataManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.core.util.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.hemmah.Utils.SharedPrefUtils;
import com.google.hemmah.model.MeetingRoom;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class StompClientManager {
    private  String mUserToken;
    private static final String STOMP_API = "wss://api.hemmah.live/ws";
    public static final String DISABLED_SEND_TOPIC = "/app/help_call/ask";
    public static final String DISABLED_SUBSCRIBE_TOPIC = "/user/help_call/ask";
    public static final String VOLUNTEER_SEND_TOPIC = "/app/help_call/answer";
    public static final String VOLUNTEER_SUBSCRIBE_TOPIC = "/user/help_call/answer";
    private Context mContext;
    Gson mGson = new GsonBuilder().create();
    private StompClient mStompClient;
    private SharedPreferences mSharedPreferences;

    public StompClientManager(Context context, String token) {
        this.mContext = context;
        this.mSharedPreferences = mContext.getSharedPreferences(SharedPrefUtils.FILE_NAME,Context.MODE_PRIVATE);
        this.mUserToken = token;
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, STOMP_API);
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Timber.d("Stomp connection opened");
                            break;
                        case ERROR:
                            Timber.d(lifecycleEvent.getException(), "Stomp connection error");
                            break;
                        case CLOSED:
                            Timber.d("Stomp connection closed");
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Timber.d("Stomp failed server heartbeat");
                            break;
                    }
                });
    }


    public void subscribeOnTopic(String receiveTopic, Consumer<MeetingRoom> callback) {
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", mUserToken));
        mStompClient.topic(receiveTopic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    MeetingRoom room = mGson.fromJson(topicMessage.getPayload(), MeetingRoom.class);
                    callback.accept(room);
                    Timber.d("Received " + room.toString());
                }, throwable -> {
                    Timber.d(throwable, "Error on subscribe topic");
                    Toast.makeText(mContext, "Failed to Connect", Toast.LENGTH_SHORT).show();
                });
        mStompClient.connect(headers);
    }
    public void sendToStomp(String topic, String message) {
        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader("Authorization", mUserToken));
        mStompClient.send(topic, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }




    public void  discconectStomp(){
        mStompClient.disconnect();
    }

}
