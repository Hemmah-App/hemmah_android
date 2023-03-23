package com.google.hemmah.presentation.videocall;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.hemmah.R;


public class HelpCallRequestNotification {

    public static final String CHANNEL_1_ID = "call notification";
    public static final String CHANNEL_1_NAME = "Call Notification";
    public static final String CHANNEL_1_DESCRIPTION = "This notification appears when a disabled needs a video call help";
    protected Context mContext;
    private final NotificationManagerCompat mNotificationManagerCompat;
    private PendingIntent mPendingIntent;

    public HelpCallRequestNotification(Context context,PendingIntent pendingIntent) {
        this.mContext = context;
        this.mPendingIntent = pendingIntent;
        mNotificationManagerCompat = NotificationManagerCompat.from(context);
    }

    public void makeCallNotification(int notificationId, String notificationContentText) {
        //this group id to group all the same notifications in inbox style
        final String NOTIFICATION_GROUP_KEY = "com.google.hemmah.ui.volunteer";
        //inflating the custom notification layout
        RemoteViews collapsedNotification = new RemoteViews(mContext.getPackageName(), R.layout.incoming_expanded_call_notification);
        RemoteViews expandedNotification = new RemoteViews(mContext.getPackageName(), R.layout.incoming_expanded_call_notification);
        Notification notification = new NotificationCompat.Builder(mContext.getApplicationContext(), CHANNEL_1_ID)
                .setSmallIcon(R.mipmap.app_white_logo_round)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedNotification)
                .setCustomBigContentView(expandedNotification)
                .setColor(mContext.getColor(R.color.colorOnPrimary))
                .setGroup(NOTIFICATION_GROUP_KEY)
                .build();
        collapsedNotification.setTextViewText(R.id.callType, notificationContentText);
        expandedNotification.setTextViewText(R.id.callType, notificationContentText);
        expandedNotification.setOnClickPendingIntent(R.id.btnAnswer, mPendingIntent);

        mNotificationManagerCompat.notify(notificationId, notification);
    }



}
