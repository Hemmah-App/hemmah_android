package com.google.hemmah.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.hemmah.api.AuthApi;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VideoCallService extends Service {

    AuthApi webServices;

    @Inject
    public VideoCallService(AuthApi webServices) {
        this.webServices = webServices;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}