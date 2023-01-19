package com.google.hemmah.ui.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

public class App extends Application {
    public static final String CHANNEL_1_ID  = "call notification";
    @Override
    public void onCreate() {
        super.onCreate();
        makeNotificationChannel();

    }




    public void makeNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel callNotification = new NotificationChannel(CHANNEL_1_ID,
                    "Call Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            callNotification.setDescription("This notification appears when a disabled needs a video call help");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(callNotification);
        }
    }
}
