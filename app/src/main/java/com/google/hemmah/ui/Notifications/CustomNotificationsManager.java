package com.google.hemmah.ui.Notifications;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.hemmah.R;


public class CustomNotificationsManager {
    protected Context mContext;
    private final NotificationManagerCompat mNotificationManagerCompat;

    public CustomNotificationsManager(Context context) {
        this.mContext = context;
        mNotificationManagerCompat = NotificationManagerCompat.from(context);
    }


    public void makeCallNotification(int notificationId) {
        //this group id to group all the same notifications in inbox style
        final String NOTIFICATION_GROUP_KEY = "com.google.hemmah.ui.volunteer";
        //inflating the custom notification layout
        RemoteViews collapsedNotification = new RemoteViews(mContext.getPackageName(), R.layout.incoming_expanded_call_notification);
        RemoteViews expandedNotification = new RemoteViews(mContext.getPackageName(), R.layout.incoming_expanded_call_notification);
        //pending intent on pressing the notification , it moves the user to the call
//        PendingIntent goToVideoCallIntent = makeNotificationPendingIntent();
        Notification notification = new NotificationCompat.Builder(mContext.getApplicationContext(), NotificationChannelManager.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.hemmah_logo_nobg)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedNotification)
                .setCustomBigContentView(expandedNotification)
                .setColor(mContext.getColor(R.color.colorOnPrimary))
                .setGroup(NOTIFICATION_GROUP_KEY)
                .build();

        mNotificationManagerCompat.notify(notificationId, notification);
    }

//    public PendingIntent makeNotificationPendingIntent() {
////        Intent intent = new Intent(mContext, JitsiMeetActivity.class);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        return PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//    }

    public static class NotificationChannelManager extends Application {
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
}
