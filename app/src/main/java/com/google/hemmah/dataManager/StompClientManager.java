package com.google.hemmah.dataManager;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.hemmah.Utils.ModelJson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class StompClientManager {
    private static final String stompHeaderTempToken = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYWJkdWxsYWh3c2RkNTUiLCJleHAiOjE2NzU3MjIzODQsImlhdCI6MTY3NTY4NjM4NCwicm9sZXMiOiJVU0VSLERJU0FCTEVEIn0.f38Cb3aHOIsS0VV2tISk3W8lb1MVYZIphXX07CzZ2Iit8IPDVXwPqTn7E28EIbhXgp_aPJDRTTQIfTrJdfaTCNNcUdv3r87aEDvT5QURLeAO4aFLsYkdW9qPPsnCFBzhfUhrZYp3x6g9Jf58H70DYrSQV4BS-odQ3c9OQ9PeVBu_ySIKG5sZmqWELcx02qAKkbbj_pQu1VKHX_3UElIPiyGjRoB7WewZs0ZuvZrYTXl46acH9OyEYyYtLgITVsapthU1s3_JcP-8kwooE0r5WlS7xt7uVm6j_ucCE0j519phlA_oRllIQQFu6jOVw8OAEcLCplEdXvXQaiZZvbC-Iw";
    private static final String stompApi = "wss://api.hemmah.live/ws";
    private static final String TAG = "StompMessages";
    private Gson mGson = new GsonBuilder().create();
    private StompClient mStompClient;

    public StompClientManager(String stompHeaderToken) {
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, stompApi);
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        Disposable dispLifecycle = mStompClient.lifecycle()
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




    public void subscribeOnTopic(String topic ,String stompHeaderToken ) {
        List<StompHeader> headers  = new ArrayList<>();
        headers.add(new StompHeader("Authorization", stompHeaderToken));
        Disposable dispTopic = mStompClient.topic(topic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                Log.d(TAG, "Received " + topicMessage.getPayload());
//                    convertJsonToString(mGson.fromJson(topicMessage.getPayload(), ModelJson.class));
                }, throwable -> {
                    Log.d(TAG, "Error on subscribe topic", throwable);
                });
        mStompClient.connect(headers);
    }

    private void convertJsonToString(ModelJson fromJson) {
        Log.d(TAG, fromJson.getMessage());
    }

}
