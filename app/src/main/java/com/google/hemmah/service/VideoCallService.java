package com.google.hemmah.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.hemmah.api.ApiClient;
import com.google.hemmah.api.WebServices;

import retrofit2.Call;
import retrofit2.Retrofit;

public class VideoCallService extends Service {
    private WebServices webServices;

    public VideoCallService() {
        webServices = ApiClient.getRetrofit().create(WebServices.class);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}