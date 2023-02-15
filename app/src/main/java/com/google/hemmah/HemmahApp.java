package com.google.hemmah;

import static com.google.hemmah.view.Notifications.HelpCallRequestNotification.CHANNEL_1_DESCRIPTION;
import static com.google.hemmah.view.Notifications.HelpCallRequestNotification.CHANNEL_1_ID;
import static com.google.hemmah.view.Notifications.HelpCallRequestNotification.CHANNEL_1_NAME;
import android.app.Application;
import android.app.NotificationChannel;
import android.os.Build;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public final class HemmahApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        makeNotificationChannel();
    }

    public void makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel callNotification = new NotificationChannel(
                    CHANNEL_1_ID, CHANNEL_1_NAME, android.app.NotificationManager.IMPORTANCE_HIGH);

            callNotification.setDescription(CHANNEL_1_DESCRIPTION);
            android.app.NotificationManager manager = getSystemService(android.app.NotificationManager.class);
            manager.createNotificationChannel(callNotification);
        }
    }



}
