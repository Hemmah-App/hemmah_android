package com.google.hemmah.ui.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.os.Build;


public class NotificationChannelManager extends Application {
    public static final String CHANNEL_1_ID = "call notification";
    public static final String CHANNEL_1_NAME = "Call Notification";
    public static final String CHANNEL_1_DESCRIPTION = "This notification appears when a disabled needs a video call help";
    @Override
    public void onCreate() {
        super.onCreate();
        makeNotificationChannel();
    }


    public void makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel callNotification = new NotificationChannel(CHANNEL_1_ID,
                    CHANNEL_1_NAME,
                    android.app.NotificationManager.IMPORTANCE_HIGH);
            callNotification.setDescription(CHANNEL_1_DESCRIPTION);
            android.app.NotificationManager manager = getSystemService(android.app.NotificationManager.class);
            manager.createNotificationChannel(callNotification);
        }
    }



}
