package com.google.hemmah;

import static com.google.hemmah.view.Notifications.HelpCallRequestNotification.CHANNEL_1_DESCRIPTION;
import static com.google.hemmah.view.Notifications.HelpCallRequestNotification.CHANNEL_1_ID;
import static com.google.hemmah.view.Notifications.HelpCallRequestNotification.CHANNEL_1_NAME;
import android.app.Application;
import android.app.NotificationChannel;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

@HiltAndroidApp
public final class HemmahApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        makeNotificationChannel();
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

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

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    Log.e("APP_ERROR","error app",t);
                } else if (priority == Log.WARN) {
                    Log.w("APP_ERROR","warning app",t);
                }
            }
        }
    }
}
