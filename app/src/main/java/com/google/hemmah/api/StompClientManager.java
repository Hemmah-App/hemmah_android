package com.google.hemmah.api;

import android.app.Service;
import android.content.Intent;

import android.os.HandlerThread;
import android.os.IBinder;

import androidx.annotation.Nullable;


public class StompClientManager extends Service {
    public StompClientManager(String stompApi){


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
